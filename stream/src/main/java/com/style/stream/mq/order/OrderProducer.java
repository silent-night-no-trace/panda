package com.style.stream.mq.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author leon
 * @date 2020-09-03 13:57:27
 */
@EnableBinding(OrderProcess.class)
public class OrderProducer implements CommandLineRunner {

	@Autowired
	@Qualifier(OrderProcess.OUTPUT)
	private MessageChannel output;

	@Override
	public void run(String... args) throws Exception {
		output.send(MessageBuilder.withPayload("order create success").build());
	}
}
