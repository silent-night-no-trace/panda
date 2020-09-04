package com.style.stream.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 消息接收端
 *
 * @author leon
 * @date 2020-08-31 14:45:57
 */
public interface Sink {
	String INPUT = "input";

	/**
	 * 消息订阅
	 *
	 * @return SubscribableChannel
	 */
	@Input(INPUT)
	SubscribableChannel input();
}
