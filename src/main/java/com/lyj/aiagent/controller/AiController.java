package com.lyj.aiagent.controller;


import com.lyj.aiagent.app.LoveApp;
import com.lyj.aiagent.app.Manus;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private LoveApp loveApp;



    @Resource
    private ToolCallback[] allTools;


    @Resource
    private ChatModel dashscopeChatModel;




    /**
     * 流式调用 LoveApp恋爱大师应用
     *
     * @param message
     * @return
     */

    @GetMapping(value = "/love_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithLoveAppSSE(String message, String chatId) {
        return loveApp.doChatStream(message, chatId);
    }




    /**
     * 流式调用 Manus 超级智能体应用
     *
     * @param message
     * @return
     */
    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        Manus manus = new Manus(allTools, dashscopeChatModel);
        return manus.runStream(message);
    }




}
