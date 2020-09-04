package com.style.stream.mq;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

/**
 * @author leon
 * @date 2020-08-31 14:49:18
 */
@EnableBinding(Sink.class)
public class Consumer {

	/**
	 * // 监听 binding 为 Sink.INPUT 的消息
	 * @param message message
	 */
	@StreamListener(Sink.INPUT)
	public void receive(Message<String> message) {
		System.out.println("接收到的消息:" + message.getPayload());
	}
}
