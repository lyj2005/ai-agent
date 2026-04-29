package com.lyj.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.lyj.aiagent.constant.FileConstant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PDFGenerationToolTest {

    @Test
    void testGeneratePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        FileUtil.mkdir(FileConstant.FILE_SAVE_DIR + "/pdf");

        String fileName = "test.pdf";
        String content = """
                [
                  {"type":"text","content":"这是一份测试文档"},
                  {"type":"text","content":"第一章 简介"},
                  {"type":"text","content":"测试中文内容"}
                ]
                """;

        String result = tool.generatePDF(fileName, content);
        String filePath = FileConstant.FILE_SAVE_DIR + "/pdf/" + fileName;

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.startsWith("PDF generated successfully"), result);
        Assertions.assertTrue(FileUtil.exist(filePath), result);
        System.out.println(result);

        FileUtil.del(filePath);
    }
}
