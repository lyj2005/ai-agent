package com.lyj.aiagent.ragplus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/**
 * 检索增强器
 */
@Slf4j
public class LoveAppRagCustomAdvisorFactory {


    public static Advisor createLoveAppRagCustomAdvisor(VectorStore vectorStore, String status) {
        
        
        Filter.Expression expression = new FilterExpressionBuilder()
                .eq("status", status)
                .build();
        
        
        /**
        文档检索器
        */
        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .filterExpression(expression) // 过滤条件
                .similarityThreshold(0.5) // 相似度阈值
                .topK(3) // 返回文档数量
                .build();
        
        
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                //添加自定义错误处理器
                .queryAugmenter(LoveAppContextualQueryAugmenterFactory.createInstance())
                .build();
    }

    
}