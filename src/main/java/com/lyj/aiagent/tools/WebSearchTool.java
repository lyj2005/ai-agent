package com.lyj.aiagent.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * WebSearchTool 类是一个网络搜索工具，用于通过百度搜索引擎进行网页搜索。
 * 该类封装了与SearchAPI的交互，提供了简单的搜索功能。
 */
public class WebSearchTool {

    // SearchAPI 的搜索接口地址，用于发送搜索请求
    private static final String SEARCH_API_URL = "https://www.searchapi.io/api/v1/search";

    // 存储API密钥，用于身份验证
    private final String apiKey;

    /**
     * 构造函数，初始化WebSearchTool实例
     * @param apiKey 用于API调用的密钥
     */
    public WebSearchTool(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * 使用百度搜索引擎进行网页搜索
     * @param query 搜索关键词
     * @return 返回搜索结果的JSON字符串，最多包含前5条结果
     */
    @Tool(description = "Search for information from Baidu Search Engine")
    public String searchWeb(
            @ToolParam(description = "Search query keyword") String query) {
        // 创建参数映射，包含搜索查询和API密钥
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("q", query);
        paramMap.put("api_key", apiKey);
        paramMap.put("engine", "baidu");
        try {
            // 发送HTTP GET请求获取搜索结果
            String response = HttpUtil.get(SEARCH_API_URL, paramMap);
            // 取出返回结果的前 5 条
            JSONObject jsonObject = JSONUtil.parseObj(response);
            // 提取 organic_results 部分
            JSONArray organicResults = jsonObject.getJSONArray("organic_results");
            List<Object> objects = organicResults.subList(0, 5);
            // 拼接搜索结果为字符串
            String result = objects.stream().map(obj -> {
                JSONObject tmpJSONObject = (JSONObject) obj;
                return tmpJSONObject.toString();
            }).collect(Collectors.joining(","));
            return result;
        } catch (Exception e) {
            return "Error searching Baidu: " + e.getMessage();
        }
    }
}


