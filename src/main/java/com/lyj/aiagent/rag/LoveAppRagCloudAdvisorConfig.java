package com.lyj.aiagent.rag;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * 自定义基于阿里云知识库服务的 RAG 增强顾问
 */
@Configuration
public class LoveAppRagCloudAdvisorConfig {


    @Value("${spring.ai.dashscope.api-key}")
   private String dashScopeApiKey;


    //创建知识库advisor
    @Bean
    public Advisor loveAppRagCloudAdvisor() {
        //1. 调用api
        DashScopeApi dashScopeApi = new DashScopeApi(dashScopeApiKey);
        //2. 创建文档检索器
        final String KNOWLEDGE_INDEX = "恋爱大师";
        DocumentRetriever documentRetriever = new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder()
                        .withIndexName(KNOWLEDGE_INDEX)
                        .build());
        //3. 返回结果
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .build();
    }




}