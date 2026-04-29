package com.lyj.aiagent.demo.invoke;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class HttpDemo {
    
    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    
    public static String callWithHutool(String apiKey) {
        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.set("model", "qwen-plus");
        
        // 构建 messages 数组
        JSONArray messages = new JSONArray();
        
        // system 消息
        JSONObject systemMsg = new JSONObject();
        systemMsg.set("role", "system");
        systemMsg.set("content", "You are a helpful assistant.");
        messages.add(systemMsg);
        
        // user 消息
        JSONObject userMsg = new JSONObject();
        userMsg.set("role", "user");
        userMsg.set("content", "你是谁？");
        messages.add(userMsg);
        
        // 设置 input
        JSONObject input = new JSONObject();
        input.set("messages", messages);
        requestBody.set("input", input);
        
        // 设置 parameters
        JSONObject parameters = new JSONObject();
        parameters.set("result_format", "message");
        requestBody.set("parameters", parameters);
        
        // 发送 POST 请求
        HttpResponse response = HttpRequest.post(API_URL)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .execute();
        
        // 返回响应结果
        return response.body();
    }




    public static void main(String[] args) {
        // 从环境变量获取 API Key
        String apiKey = TestApiKey.API_KEY;

        try {
            String result = callWithHutool(apiKey);
            
            // 格式化输出 JSON 结果
            JSONObject jsonResponse = JSONUtil.parseObj(result);
            System.out.println(JSONUtil.toJsonPrettyStr(jsonResponse));
            
        } catch (Exception e) {
            System.err.println("调用 API 失败: " + e.getMessage());
            e.printStackTrace();
        }
    }




}
