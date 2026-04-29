package com.lyj.aiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

/**
 * WebScrapingTool类是一个用于网页抓取的工具类
 * 提供了通过Jsoup库获取网页HTML内容的功能
 */
public class WebScrapingTool {



    /**
     * 抓取指定URL网页的内容
     * @param url 要抓取的网页URL地址
     * @return 返回网页的完整HTML内容，如果发生错误则返回错误信息
     */
    @Tool(description = "Scrape the content of a web page")
    public String scrapeWebPage(@ToolParam(description = "URL of the web page to scrape") String url) {
        try {
            // 使用Jsoup连接指定URL并获取文档对象
            Document doc = Jsoup.connect(url).get();
            // 返回文档的HTML内容
            return doc.html();
        } catch (IOException e) {
            // 捕获并返回可能的IO异常信息
            return "Error scraping web page: " + e.getMessage();
        }
    }



}