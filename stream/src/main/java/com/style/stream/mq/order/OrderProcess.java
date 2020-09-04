package com.style.stream.mq.order;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 定义订单处理的 消息接收 消息发送通道
 *
 * @author leon
 * @date 2020-09-03 13:42:49
 */
public interface OrderProcess {

	String OUTPUT = "outputOrder";
	String INPUT = "inputOrder";

	/**
	 * 订单消息发送
	 *
	 * @return MessageChannel
	 */
	@Output(OUTPUT)
	MessageChannel outputOrder();

	/**
	 * 订单消息接收
	 *
	 * @return SubscribableChannel
	 */
	@Input(INPUT)
	SubscribableChannel inputOrder();

}
