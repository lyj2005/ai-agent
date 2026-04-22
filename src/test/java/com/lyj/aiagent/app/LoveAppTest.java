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


    @Test
    void doChatWithTool() {

        // 测试联网搜索问题的答案
        testMessage("周末想带女朋友去上海约会，推荐几个适合情侣的小众打卡地？");

        // 测试网页抓取：恋爱案例分析
        testMessage("最近和对象吵架了，看看编程导航网站（codefather.cn）的其他情侣是怎么解决矛盾的？");

        // 测试资源下载：图片下载
        testMessage("直接下载一张适合做手机壁纸的星空情侣图片为文件");

        // 测试终端操作：执行代码
        testMessage("执行 Python3 脚本来生成数据分析报告");

        // 测试文件操作：保存用户档案
        testMessage("保存我的恋爱档案为文件");

        // 测试 PDF 生成
        testMessage("生成一份‘七夕约会计划’PDF，包含餐厅预订、活动流程和礼物清单");
    }


    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String result = loveApp.doChatWithTool(message, chatId);
        Assertions.assertNotNull(result);
    }


    @Test
    void doChatWithMCP() {

        // 生成一个唯一的聊天ID，用于标识本次聊天会话
        String chatId = UUID.randomUUID().toString();

        // 定义要发送的聊天内容
        String message = "我的另一半居住在上海静安区，请帮我找到 5 公里内合适的约会地点";
        // 调用loveApp的doChat方法执行聊天操作
        String result = loveApp.doChatWithMCP(message, chatId);
        Assertions.assertNotNull(result);
    }



}