package com.lyj.aiagent.app;


import com.lyj.aiagent.advisor.MyLoggerAdvisor;
import com.lyj.aiagent.chatmemory.FileBasedChatMemory;
import com.lyj.aiagent.ragplus.QueryRewriter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

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
     * 构造器。根据名称注入ChatClient
     * @param dashscopeChatModel
     */
    public LoveApp(ChatModel dashscopeChatModel) {

        //基于内存的对话记忆
        ChatMemory chatInMemory = new InMemoryChatMemory();

        //基于文件的对话记忆，保存到目录tmp/chat-memory
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        ChatMemory chatFileMemory = new FileBasedChatMemory(fileDir);

        //创建client
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        //保存对话记忆
//                        new MessageChatMemoryAdvisor(chatInMemory),
                        new MessageChatMemoryAdvisor(chatFileMemory),
                        // 自定义日志 Advisor，可按需开启
                        new MyLoggerAdvisor()
                        // 自定义推理增强 Advisor 可按需开启
//                        new ReReadingAdvisor()
                )
                .build();
    }




    /**
     * AI 基础对话（支持多轮对话记忆）
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId) {

        //调用 chatClie nt 对象
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();

        //解析结果
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        //返回结果
        return content;

    }





    //定义恋爱报告类，包含标题和建议列表
    /*private static class LoveReport {
        private String title;
        private List<String> suggestions;
    }*/

    record LoveReport(String title, List<String> suggestions) {

    }


    /**
     * AI 恋爱报告功能（结构化输出）
     * @param message
     * @param chatId
     * @return
     */
    public LoveReport doChatWithReport(String message, String chatId) {

        //调用 chatClie nt 对象
        LoveReport loveReport = chatClient.prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveReport.class);

        log.info("LoveReport: {}", loveReport);
        return loveReport;

    }






    //引入基于本地的向量存储
    @Resource
    private VectorStore loveAppVectorStore;



    //引入云RAG服务
    @Resource
    private Advisor loveAppRagCloudAdvisor;


    //引入基于PGvector的向量存储
    @Resource
    private VectorStore pgVectorVectorStore;



    //引入查询重写器
    @Resource
    private QueryRewriter queryRewriter;



    /**
     * RAG 知识库进行对话
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithRAG(String message, String chatId) {

        //重写message
        String rewrittenMessage = queryRewriter.doQueryRewrite(message);

        ChatResponse response = chatClient
                .prompt()
                //使用改写后的查询
                .user(rewrittenMessage)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                //应用RAG知识库问答
                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                //应用RAG检索增强服务（基于云知识库服务）
//                .advisors(loveAppRagCloudAdvisor)
                //应用RAG检索增强服务（基于PGVector向量存储）
//                .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                //应用RAG检索增强服务（基于 RetrievalAugmentationAdvisor 查询增强顾问）
                /*.advisors(LoveAppRagCustomAdvisorFactory.createLoveAppRagCustomAdvisor(
                        loveAppVectorStore, "单身"
                ))*/
                .call()
                .chatResponse();

        //解析结果
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        //返回结果
        return content;

    }




    //引入工具包
    @Resource
    private ToolCallback[] allTools;



    /**
     * AI 恋爱报告功能（支持调用工具）
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithTool(String message, String chatId) {

        //调用 chatClie nt 对象
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .tools(allTools)
                .call()
                .chatResponse();

        //解析结果
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        //返回结果
        return content;

    }




    //引入MCP
    @Resource
    private ToolCallbackProvider toolCallbackProvider;





    /**
     * AI 恋爱报告功能（调用 MCP 服务）
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithMCP(String message, String chatId) {

        //调用 chatClie nt 对象
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();

        //解析结果
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        //返回结果
        return content;

    }



    /**
     * AI服务化  --  流式返回（基于云知识库）
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> doChatByStream(String message, String chatId) {

        return   chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(loveAppRagCloudAdvisor)
                .stream()
                .content();

    }





}
