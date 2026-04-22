package com.lyj.aiagent.constant;

public interface FileConstant {

    /**
     * 文件保存目录
     * 该常量用于定义系统中文件保存的默认目录路径
     * 路径为用户当前工作目录下的tmp文件夹
     * 例如：如果用户工作目录为"C:/project"，则文件将保存在"C:/project/tmp"目录下
     */
    String FILE_SAVE_DIR = System.getProperty("user.dir") + "/tmp";


}
