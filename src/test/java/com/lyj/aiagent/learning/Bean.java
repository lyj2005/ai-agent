package com.lyj.aiagent.learning;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;


/**
 * 定义 bean
 */
public class Bean {

/*
    //创建知识库advisor
    @Bean
    Advisor loveAppRagCloudAdvisor() {
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
    }*/


/*
    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {


        //1.初始化向量数据库
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel)
                .build();
        //2.  加载文档
        List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
        //3. 保存数据库
        simpleVectorStore.add(documents);
        //4. 返回结果
        return simpleVectorStore;

    }*/









}
