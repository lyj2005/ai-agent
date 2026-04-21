package com.lyj.aiagent.rag;

import com.lyj.aiagent.ragplus.MyKeywordEnricher;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 基于本地的向量数据库
 */

@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private MyKeywordEnricher myKeywordEnricher;
    

    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {

    
        //1.初始化向量数据库
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel)
                .build();
        //2.  加载文档
        List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
        // 自动补充关键词元信息
        List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(documents);
        simpleVectorStore.add(enrichedDocuments);
        //3. 保存基于内存的数据库
        simpleVectorStore.add(documents);
        //4. 返回结果
        return simpleVectorStore;
    
    }


}