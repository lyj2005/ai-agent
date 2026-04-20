package com.lyj.aiagent.app;

import cn.hutool.core.lang.UUID;
import com.google.errorprone.annotations.Var;
import jakarta.annotation.Resource;
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
        String message = "我是lyj";
        loveApp.doChat(message, chatId);

       /* //2. 第二轮
        message = "我的女朋友叫yj";
        loveApp.doChat(message, chatId);

        //3. 第三轮
        message = "我的女朋友叫什么名字";
        loveApp.doChat(message, chatId);*/



    }




}