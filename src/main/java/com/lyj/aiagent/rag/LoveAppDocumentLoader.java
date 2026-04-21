package com.lyj.aiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class LoveAppDocumentLoader {

    //1. 创建一个 ResourcePatternResolver 对象，用于加载资源文件
    private final ResourcePatternResolver resourcePatternResolver;

    LoveAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }


    //2. 实现加载文档的方法.负责读取所有 Markdown 文档并转换为 Document 列表
    public List<Document> loadMarkdowns() {

        List<Document> allDocuments = new ArrayList<>();


        try {
            // 这里可以修改为你要加载的多个 Markdown 文件的路径模式
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");



            for (Resource resource : resources) {

                //1. 得到文件名
                String fileName = resource.getFilename();
//                String status = fileName.substring(fileName.length()-6,fileName.length()-4);


                //2. 引用官方文档  ，定义文档加载器
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename", fileName)
//                        .withAdditionalMetadata("status",status)
                        .build();
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);


                allDocuments.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("Markdown 文档加载失败", e);
        }
        return allDocuments;
    }

    }

