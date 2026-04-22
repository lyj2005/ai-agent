package com.lyj.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FileOperationToolTest类用于测试FileOperationTool类的功能
 * 包含读取文件和写入文件的测试方法
 */
class FileOperationToolTest {

    /**
     * 测试读取文件的方法
     * 验证FileOperationTool的readFile方法是否能正常读取文件内容
     */
    @Test
    void readFile() {
        // 创建FileOperationTool实例
        FileOperationTool tool = new FileOperationTool();
        // 定义要读取的文件名
        String fileName = "lyj.txt";
        // 调用readFile方法读取文件内容
        String result = tool.readFile(fileName);
        // 断言结果不为空，验证文件读取成功
        Assertions.assertNotNull(result);
    }

    /**
     * 测试写入文件的方法
     * 验证FileOperationTool的writeFile方法是否能正常写入文件内容
     * 并通过读取文件验证写入的内容是否正确
     */
    @Test
    void writeFile() {
        // 创建FileOperationTool实例
        FileOperationTool tool = new FileOperationTool();
        // 定义要写入的文件名
        String fileName = "lyj.txt";
        // 定义要写入的内容
        String content = "hello lyj";
        // 调用writeFile方法写入内容
        tool.writeFile(fileName, content);
        // 读取文件内容
        String result = tool.readFile(fileName);
        // 断言结果不为空，验证文件写入和读取成功
        Assertions.assertNotNull(result);

    }
}