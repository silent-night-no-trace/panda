package com.style.stream.mq.group;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 分组
 *
 * @author leon
 * @date 2020-09-03 14:16:21
 */
public interface GroupProcess {

	String GROUP_OUTPUT = "groupOutput";
	/**
	 * 定义两个消费者
	 */
	String GROUP_INPUT = "groupInput";
	String GROUP_INPUT2 = "groupInput2";

	/**
	 * 分组消息发送
	 *
	 * @return MessageChannel
	 */
	@Output(GROUP_OUTPUT)
	MessageChannel groupOutput();

	/**
	 * 分组消息接收
	 *
	 * @return SubscribableChannel
	 */
	@Input(GROUP_INPUT)
	SubscribableChannel groupInput();

	/**
	 * 分组消息接收
	 *
	 * @return SubscribableChannel
	 */
	@Input(GROUP_INPUT2)
	SubscribableChannel groupInput2();
}
