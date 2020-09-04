package com.style.stream.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 发送端
 * 声明了一个消息输出流，也就是消息的生 产者。
 *
 * @author leon
 * @date 2020-08-31 14:16:14
 */
public interface Source {
	String OUTPUT = "output";

	/**
	 * 消息发送通道
	 * @return MessageChannel
	 */
	@Output(OUTPUT)
	MessageChannel output();
}
