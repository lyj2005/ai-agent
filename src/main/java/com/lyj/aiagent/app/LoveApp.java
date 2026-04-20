package com.lyj.aiagent.app;


import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.lyj.aiagent.advisor.MyLoggerAdvisor;
import com.lyj.aiagent.advisor.ReReadingAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class LoveApp {


    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";


    /**
     * 根据名称注入ChatClient
     * @param dashscopeChatModel
     */
    public LoveApp(ChatModel dashscopeChatModel) {
        //1.  初始化基于内存的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();
        //2. 创建client
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor(),
                        new ReReadingAdvisor()
                )
                .build();
    }


    /**
     * 编写对话方法
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId) {

        //1. 调用 chatClie nt 对象
        ChatResponse response = chatClient
                //2. 传入用户 Pr⁢⁢⁢ompt，并且给 advisor 指定对话 id 和⁢⁢对话⁢记忆大小
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();

        //3. 得到内容
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        //4. 返回结果
        return content;

    }








}
