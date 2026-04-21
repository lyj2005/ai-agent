package com.lyj.aiagent.app;

import cn.hutool.core.lang.UUID;
import com.google.errorprone.annotations.Var;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class LoveAppTest {


    @Resource
    private LoveApp loveApp;


    /**
     * 测试多轮对话
     */
    @Test
    void doChat() {

        String chatId = UUID.randomUUID().toString();

        //1. 第一轮
//        String message = "我是lyj";
        String message = "婚后与伴侣家人产生矛盾，如何妥善解决？";
        String result = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(result);
        

       /* //2. 第二轮
        message = "我的女朋友叫yj";
        loveApp.doChat(message, chatId);

        //3. 第三轮
        message = "我的女朋友叫什么名字";
        loveApp.doChat(message, chatId);*/



    }


/**
 * 测试方法：用于测试带有报告功能的聊天功能
 * 该方法会生成一个唯一的聊天ID，并执行聊天操作
 */
    @Test
    void doChatWithReport() {
        // 生成一个唯一的聊天ID，用于标识本次聊天会话
        String chatId = UUID.randomUUID().toString();

        // 定义要发送的聊天内容
        String message = "我是lyj";
        // 调用loveApp的doChat方法执行聊天操作
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);

    }


    @Test
    void doChatWithRAG() {
        // 生成一个唯一的聊天ID，用于标识本次聊天会话
        String chatId = UUID.randomUUID().toString();

        // 定义要发送的聊天内容
        String message = "婚后与伴侣家人产生矛盾，如何妥善解决？";
        // 调用loveApp的doChat方法执行聊天操作
        String result = loveApp.doChatWithRAG(message, chatId);
        Assertions.assertNotNull(result);
        
    }




    @Test
    void doChatWithRAGPlus() {
        // 生成一个唯一的聊天ID，用于标识本次聊天会话
        String chatId = UUID.randomUUID().toString();

        // 定义要发送的聊天内容
        String message = "我已经结婚了，婚后与伴侣家人产生矛盾，如何妥善解决？";
        // 调用loveApp的doChat方法执行聊天操作
        String result = loveApp.doChatWithRAGPlus(message, chatId);
        Assertions.assertNotNull(result);
    }

    

}