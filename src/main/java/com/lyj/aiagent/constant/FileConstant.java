package com.lyj.aiagent.constant;

/**
 * 文件常量
 */
public interface FileConstant {

    /**
     * 文件保存目录
     */
    String FILE_SAVE_DIR = System.getProperty("user.dir") + "/tmp";

    /**
     * 下载资源目录（图片等先落盘于此，PDF 工具仅按文件名从此目录读取）
     */
    String FILE_DOWNLOAD_DIR = FILE_SAVE_DIR + "/download";
}
