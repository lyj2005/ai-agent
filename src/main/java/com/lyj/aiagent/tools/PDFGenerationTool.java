package com.lyj.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.lyj.aiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

/**
 * 按块（文本 / 图片）生成 PDF 的工具类。
 */
public class PDFGenerationTool {

    private static final String JSON_STRUCT_HINT = """
            参数 content 必须是 JSON 数组，元素为对象且仅含两个字段 type 与 content。示例：\
            [{"type":"text","content":"第一章 概述……"},{"type":"image","content":"chart.png"}]\
            。type 仅允许 text 或 image（大小写不敏感）；text 的 content 为段落文字且全文中须含汉字；\
            image 的 content 仅为文件名（无路径），文件须已存在于 tmp/download。""";

    private static final String MSG_FILE_NAME_EMPTY = "[参数错误] fileName 不能为空或空白。请仅传文件名（含 .pdf 后缀），例如 report.pdf。" + JSON_STRUCT_HINT;
    private static final String MSG_FILE_NAME_PATH = "[参数错误] fileName 不得包含路径分隔符或 \"..\"，请只使用纯文件名，例如 output.pdf。" + JSON_STRUCT_HINT;
    private static final String MSG_FILE_NAME_EXT = "[参数错误] fileName 必须以 .pdf 结尾（大小写不敏感），例如 document.pdf。" + JSON_STRUCT_HINT;
    private static final String MSG_CONTENT_NULL = "[参数错误] content 不能为 null。请传入 JSON 数组字符串。" + JSON_STRUCT_HINT;
    private static final String MSG_CONTENT_BLANK = "[参数错误] content 不能为空或空白。请传入 JSON 数组字符串。" + JSON_STRUCT_HINT;
    private static final String MSG_CONTENT_NOT_ARRAY = "[参数错误] content 必须是 JSON 数组字符串，例如 [{\"type\":\"text\",\"content\":\"正文\"}]。" + JSON_STRUCT_HINT;
    private static final String MSG_CONTENT_PARSE_ERROR = "[参数错误] content 不是合法的 JSON 数组字符串，解析失败：%s。" + JSON_STRUCT_HINT;
    private static final String MSG_CONTENT_EMPTY = "[参数错误] content 不能为空数组，至少包含一个内容块。" + JSON_STRUCT_HINT;
    private static final String MSG_BLOCK_NULL = "[参数错误] content[%d] 为 null，请移除空元素或补全对象。" + JSON_STRUCT_HINT;
    private static final String MSG_TYPE_EMPTY = "[参数错误] content[%d].type 不能为空。仅允许 text 或 image。" + JSON_STRUCT_HINT;
    private static final String MSG_TYPE_UNKNOWN = "[参数错误] content[%d].type=\"%s\" 非法，仅允许 text 或 image。" + JSON_STRUCT_HINT;
    private static final String MSG_BLOCK_CONTENT_EMPTY = "[参数错误] content[%d].content 不能为空或空白。" + JSON_STRUCT_HINT;
    private static final String MSG_NO_TEXT_BLOCK = "[参数错误] 至少需要一段 type 为 text 的正文块；仅图片无法通过校验。" + JSON_STRUCT_HINT;
    private static final String MSG_NEED_CHINESE = "[参数错误] 所有 text 块合起来未检测到汉字，请使用中文撰写至少一段正文后再生成。" + JSON_STRUCT_HINT;
    private static final String MSG_IMAGE_NAME_PATH = "[参数错误] content[%d] 为图片时，content 只能是 tmp/download 下的文件名，不得含路径、斜杠或 \"..\"。例如 chart.png。" + JSON_STRUCT_HINT;
    private static final String MSG_IMAGE_NOT_FOUND = "[参数错误] content[%d] 指定的图片 \"%s\" 在 tmp/download 下不存在。请先将文件下载到该目录后再仅传文件名。" + JSON_STRUCT_HINT;

    private static final String TOOL_DESC = """
            按顺序将多个内容块写入一个 PDF：块类型为 text（段落）或 image（仅文件名，从 tmp/download 读取已下载资源）。\
            输出 PDF 的 fileName 须为纯文件名且以 .pdf 结尾。content 必须传 JSON 数组字符串，不要传对象、Markdown 或自然语言。\
            中文会使用项目内置 SimSun.ttf 字体写入。""";

    private static final String PARAM_FILE_NAME = """
            仅文件名，不得含路径、斜杠、反斜杠或 \"..\"。必须以 .pdf 结尾（大小写不敏感），例如 yu-report-2024.pdf。""";

    private static final String PARAM_CONTENT = """
            JSON 数组字符串，按顺序排列；每项为 {\"type\":\"text|image\",\"content\":\"...\"}。\
            注意：必须把整个数组作为字符串传入，不能传 Markdown 或普通对象。\
            type=text 时 content 为段落文字；全文中至少有一段 text 且含汉字。\
            type=image 时 content 仅为文件名（无路径），对应文件须已在 tmp/download。示例：\
            [{\"type\":\"text\",\"content\":\"第一节 简介……\"},{\"type\":\"image\",\"content\":\"screenshot.jpg\"}]""";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Tool(description = TOOL_DESC, returnDirect = false)
    public String generatePDF(
            @ToolParam(description = PARAM_FILE_NAME) String fileName,
            @ToolParam(description = PARAM_CONTENT) String content) {
        ContentParseResult parseResult = parseContent(content);
        if (parseResult.errorMessage() != null) {
            return parseResult.errorMessage();
        }
        return generatePDF(fileName, parseResult.content());
    }

    public String generatePDF(String fileName, List<PDFContent> content) {
        String validationError = validateInputs(fileName, content);
        if (validationError != null) {
            return validationError;
        }
        String safeName = fileName.trim();
        String fileDir = FileConstant.FILE_SAVE_DIR + "/pdf";
        String filePath = fileDir + "/" + safeName;
        try {
            FileUtil.mkdir(fileDir);
            try (PdfWriter writer = new PdfWriter(filePath);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {
                String fontPath = Paths.get("src/main/resources/static/fonts/SimSun.ttf")
                        .toAbsolutePath().toString();
                PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H,
                        PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                document.setFont(font);
                for (PDFContent pdfContent : content) {
                    String type = pdfContent.type().trim();
                    if ("image".equalsIgnoreCase(type)) {
                        String imagePath = FileConstant.FILE_DOWNLOAD_DIR + "/" + pdfContent.content().trim();
                        ImageData data = ImageDataFactory.create(imagePath);
                        Image img = new Image(data);
                        img.setAutoScale(true);
                        img.setMaxWidth(document.getPdfDocument().getDefaultPageSize().getWidth() - document.getLeftMargin() - document.getRightMargin());
                        document.add(img);
                    } else {
                        Paragraph paragraph = new Paragraph(pdfContent.content());
                        document.add(paragraph);
                    }
                }
            }
            return "PDF generated successfully to: " + filePath;
        } catch (Exception e) {
            FileUtil.del(filePath);
            return "生成 PDF 时出错：" + e.getMessage() + "。请确认字体文件 src/main/resources/static/fonts/SimSun.ttf 存在、图片已在 tmp/download 且 content 仅为正确文件名，或稍后重试。";
        }
    }

    private static ContentParseResult parseContent(String content) {
        if (content == null) {
            return ContentParseResult.error(MSG_CONTENT_NULL);
        }
        String json = content.trim();
        if (json.isBlank()) {
            return ContentParseResult.error(MSG_CONTENT_BLANK);
        }
        if (!JSONUtil.isTypeJSONArray(json)) {
            return ContentParseResult.error(MSG_CONTENT_NOT_ARRAY);
        }
        try {
            List<PDFContent> blocks = OBJECT_MAPPER.readValue(json, new TypeReference<>() {
            });
            return ContentParseResult.ok(blocks);
        } catch (Exception e) {
            return ContentParseResult.error(String.format(MSG_CONTENT_PARSE_ERROR, e.getMessage()));
        }
    }

    /**
     * @return 校验失败时的提示文案；通过则返回 null
     */
    private static String validateInputs(String fileName, List<PDFContent> content) {
        if (fileName == null || fileName.isBlank()) {
            return MSG_FILE_NAME_EMPTY;
        }
        String fn = fileName.trim();
        if (fn.contains("..") || fn.contains("/") || fn.contains("\\")) {
            return MSG_FILE_NAME_PATH;
        }
        if (!fn.toLowerCase(Locale.ROOT).endsWith(".pdf")) {
            return MSG_FILE_NAME_EXT;
        }
        if (content == null) {
            return MSG_CONTENT_NULL;
        }
        if (content.isEmpty()) {
            return MSG_CONTENT_EMPTY;
        }
        StringBuilder textForHan = new StringBuilder();
        boolean hasTextBlock = false;
        for (int i = 0; i < content.size(); i++) {
            PDFContent block = content.get(i);
            if (block == null) {
                return String.format(MSG_BLOCK_NULL, i);
            }
            String type = block.type();
            if (type == null || type.isBlank()) {
                return String.format(MSG_TYPE_EMPTY, i);
            }
            String typeNorm = type.trim();
            if (!"text".equalsIgnoreCase(typeNorm) && !"image".equalsIgnoreCase(typeNorm)) {
                return String.format(MSG_TYPE_UNKNOWN, i, typeNorm);
            }
            String body = block.content();
            if (body == null || body.isBlank()) {
                return String.format(MSG_BLOCK_CONTENT_EMPTY, i);
            }
            if ("text".equalsIgnoreCase(typeNorm)) {
                hasTextBlock = true;
                textForHan.append(body);
            } else {
                String imageName = body.trim();
                if (imageName.contains("..") || imageName.contains("/") || imageName.contains("\\")) {
                    return String.format(MSG_IMAGE_NAME_PATH, i);
                }
                String imagePath = FileConstant.FILE_DOWNLOAD_DIR + "/" + imageName;
                if (!FileUtil.exist(imagePath)) {
                    return String.format(MSG_IMAGE_NOT_FOUND, i, imageName);
                }
            }
        }
        if (!hasTextBlock) {
            return MSG_NO_TEXT_BLOCK;
        }
        if (!containsHanScript(textForHan.toString())) {
            return MSG_NEED_CHINESE;
        }
        return null;
    }

    private static boolean containsHanScript(String text) {
        return text.codePoints().anyMatch(cp -> Character.UnicodeScript.of(cp) == Character.UnicodeScript.HAN);
    }

    public record PDFContent(String type, String content) {
    }

    private record ContentParseResult(List<PDFContent> content, String errorMessage) {

        private static ContentParseResult ok(List<PDFContent> content) {
            return new ContentParseResult(content, null);
        }

        private static ContentParseResult error(String errorMessage) {
            return new ContentParseResult(null, errorMessage);
        }
    }





}
