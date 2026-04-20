package com.lyj.aiagent.learning;

import org.apache.http.nio.reactor.ssl.PermanentSSLBufferManagementStrategy;
import org.springframework.ai.chat.model.ChatResponse;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

public class FluentApi {


/*
    public static void main(String[] args) {
        ChatResponse response = chatClient.prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                 .entity(LoveReport.class);
                .content();
                .chatResponse();
    }*/


}
