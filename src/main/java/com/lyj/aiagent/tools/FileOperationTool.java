package com.lyj.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.lyj.aiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;


/**
 * 文件操作工具类，提供文件读写功能
 */
public class FileOperationTool {

    /**
     * 文件存储目录路径常量
     */
    private final String FILE_DIR = FileConstant.FILE_SAVE_DIR + "/file";

    /**
     * 从文件中读取内容
     * @param fileName 要读取的文件名
     * @return 文件内容，如果读取失败则返回错误信息
     */
    @Tool(description = "Read content from a file")
    public String readFile(@ToolParam(description = "Name of the file to read") String fileName) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading file: " + e.getMessage();
        }
    }



    /**
     * 将内容写入文件
     * @param fileName 要写入的文件名
     * @param content 要写入文件的内容
     * @return 操作结果信息，成功或失败
     */
    @Tool(description = "Write content to a file")
    public String writeFile(
        @ToolParam(description = "Name of the file to write") String fileName,
        @ToolParam(description = "Content to write to the file") String content) {
        
        String filePath = FILE_DIR + "/" + fileName;
        
        try {
            // 创建目录
            FileUtil.mkdir(FILE_DIR);
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully to: " + filePath;
        } catch (Exception e) {
            return "Error writing to file: " + e.getMessage();
        }
    }
}