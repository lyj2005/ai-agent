package com.lyj.aiagent.tools;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.Map;

/**
 * 网页搜索工具
 */
@Slf4j
public class TavilySearchTool {

    private static final String TAVILY_API_URL = "https://api.tavily.com/search";

    private final String apiKey;

    public TavilySearchTool(String apiKey) {
        this.apiKey = apiKey;
    }

    @Tool(description = "Search the web for real-time information using Tavily search engine. Returns comprehensive search results with relevant information, URLs, titles, and descriptions.")
    public String searchWebByTavily(
            @ToolParam(description = "Search query keyword or question") String query,
            @ToolParam(description = "Search depth: basic (default) or advanced. Basic is faster, advanced is more comprehensive", required = false) String searchDepth,
            @ToolParam(description = "Maximum number of results to return (default: 5)", required = false) Integer maxResults) {

        String depth = (searchDepth != null && !searchDepth.isEmpty()) ? searchDepth : "basic";
        int results = (maxResults != null && maxResults > 0) ? maxResults : 5;

        log.info("开始 Tavily 搜索: '{}', 深度: {}, 最大结果数: {}", query, depth, results);

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("api_key", apiKey);
            requestBody.put("query", query);
            requestBody.put("search_depth", depth);
            requestBody.put("max_results", results);
            requestBody.put("include_answer", false);
            requestBody.put("include_images", false);

            HttpResponse response = HttpRequest.post(TAVILY_API_URL)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(requestBody))
                    .timeout(30000)
                    .execute();

            String responseBody = response.body();
            log.debug("Tavily 原始响应: {}", responseBody);

            if (!response.isOk()) {
                log.error("Tavily API 请求失败，HTTP 状态码: {}", response.getStatus());
                return "搜索失败: HTTP " + response.getStatus();
            }

            JSONObject jsonObject = JSONUtil.parseObj(responseBody);

            if (jsonObject.containsKey("error")) {
                String errorMsg = jsonObject.getStr("error");
                log.error("Tavily 返回错误: {}", errorMsg);
                return "搜索失败: " + errorMsg;
            }

            JSONArray resultsArray = jsonObject.getJSONArray("results");
            if (resultsArray == null || resultsArray.isEmpty()) {
                log.warn("未找到搜索结果");
                return "未找到相关结果，请尝试更换搜索关键词。";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("找到 ").append(resultsArray.size()).append(" 条结果:\n\n");

            for (int i = 0; i < resultsArray.size(); i++) {
                JSONObject result = resultsArray.getJSONObject(i);
                
                String title = result.getStr("title", "无标题");
                String url = result.getStr("url", "无链接");
                String content = result.getStr("content", "无内容");
                Double score = result.getDouble("score", 0.0);

                sb.append("【结果 ").append(i + 1).append("】\n");
                sb.append("标题: ").append(title).append("\n");
                sb.append("链接: ").append(url).append("\n");
                sb.append("相关度: ").append(String.format("%.2f", score)).append("\n");
                sb.append("内容: ").append(content).append("\n");
                sb.append("---\n\n");
            }

            log.info("Tavily 搜索成功，返回 {} 条结果", resultsArray.size());
            return sb.toString();

        } catch (Exception e) {
            log.error("Tavily 搜索过程中发生异常", e);
            return "搜索出错: " + e.getMessage();
        }
    }



    @Tool(description = "Search the web and get a direct answer to your question using Tavily's AI-powered search.")
    public String searchWithAnswer(
            @ToolParam(description = "Your question or search query") String query) {

        log.info("开始 Tavily 智能搜索（带答案）: '{}'", query);

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("api_key", apiKey);
            requestBody.put("query", query);
            requestBody.put("search_depth", "advanced");
            requestBody.put("max_results", 5);
            requestBody.put("include_answer", true);
            requestBody.put("include_images", false);

            HttpResponse response = HttpRequest.post(TAVILY_API_URL)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(requestBody))
                    .timeout(30000)
                    .execute();

            String responseBody = response.body();
            log.debug("Tavily 原始响应: {}", responseBody);

            if (!response.isOk()) {
                return "搜索失败: HTTP " + response.getStatus();
            }

            JSONObject jsonObject = JSONUtil.parseObj(responseBody);

            if (jsonObject.containsKey("error")) {
                return "搜索失败: " + jsonObject.getStr("error");
            }

            StringBuilder sb = new StringBuilder();

            String answer = jsonObject.getStr("answer");
            if (answer != null && !answer.isEmpty()) {
                sb.append("【智能答案】\n").append(answer).append("\n\n");
            }

            JSONArray resultsArray = jsonObject.getJSONArray("results");
            if (resultsArray != null && !resultsArray.isEmpty()) {
                sb.append("【参考来源】\n\n");
                for (int i = 0; i < Math.min(3, resultsArray.size()); i++) {
                    JSONObject result = resultsArray.getJSONObject(i);
                    sb.append(i + 1).append(". ").append(result.getStr("title", "无标题"));
                    sb.append(" - ").append(result.getStr("url", "无链接")).append("\n");
                }
            }

            log.info("Tavily 智能搜索成功");
            return sb.toString();

        } catch (Exception e) {
            log.error("Tavily 智能搜索失败", e);
            return "搜索出错: " + e.getMessage();
        }
    }



}
