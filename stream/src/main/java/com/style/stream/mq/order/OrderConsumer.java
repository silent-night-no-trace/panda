package com.style.stream.mq.order;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

/**
 * @author leon
 * @date 2020-09-03 13:53:35
 */
@EnableBinding(OrderProcess.class)
public class OrderConsumer {

	@StreamListener(value = OrderProcess.INPUT)
	public void receive(Message<String> message) {
		System.out.println("orderReceive: " + message.getPayload());
	}
}
