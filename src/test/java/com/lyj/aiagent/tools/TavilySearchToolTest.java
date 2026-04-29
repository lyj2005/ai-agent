package com.lyj.aiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLOutput;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class TavilySearchToolTest {


    @Value("${tavily.api-key}")
    private String tavilyApiKey;

    @Test
    void testTavilySearch() {
        TavilySearchTool tool = new TavilySearchTool(tavilyApiKey);

        // 测试普通搜索
        String result1 = tool.searchWebByTavily("上海静安区 约会地点", null, null);
        System.out.println(result1);

        // 测试智能搜索（带 AI 答案）
        String result2 = tool.searchWithAnswer("上海静安区 约会地点");
        System.out.println(result2);
    }


}