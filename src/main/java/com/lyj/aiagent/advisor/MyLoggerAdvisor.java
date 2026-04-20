package com.lyj.aiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisorChain;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * 自定义日志。打印 info 级别日志、并且只输出单次用户提示词和 AI 回复的文本
 */
@Slf4j
public class MyLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    /**
     * 获取当前类的简单名称
     * @return 类名
     */
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

    /**
     * 获取当前类的排序顺序
     * @return 返回0，表示优先级
     */
	@Override
	public int getOrder() {
		return 0;
	}

    /**
     * 处理普通调用请求的环绕通知
     * @param advisedRequest 请求对象
     * @param chain 调用链
     * @return 响应对象
     */
	@Override
	public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
		logRequest(advisedRequest); // 记录请求日志

		AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest); // 继续调用链

		logResponse(advisedResponse); // 记录响应日志

		return advisedResponse;
	}

    /**
     * 处理流式调用请求的环绕通知
     * @param advisedRequest 请求对象
     * @param chain 流式调用链
     * @return 响应流
     */
	@Override
	public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
		logRequest(advisedRequest); // 记录请求日志

		Flux<AdvisedResponse> responses = chain.nextAroundStream(advisedRequest); // 继续调用链

		return responses.doOnNext(this::logResponse); // 对每个响应记录日志
	}



    /**
     * 记录用户提示词日志
     * @param request 请求对象
     */
	private void logRequest(AdvisedRequest request) {
		if (request != null && request.userText() != null) {
			log.info("AI Request: {}", request.userText());
		}
	}


    /**
     * 记录AI回复日志
     * @param advisedResponse 响应对象
     */
	private void logResponse(AdvisedResponse advisedResponse) {
		if (advisedResponse != null && advisedResponse.response() != null
				&& advisedResponse.response().getResult().getOutput() != null) {
			log.info("AI Response: {}", advisedResponse.response().getResult().getOutput().getText());
		}
	}


}
