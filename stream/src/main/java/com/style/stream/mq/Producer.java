package com.style.stream.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

/**
 * @author leon
 * @date 2020-08-31 14:34:42
 */
@EnableBinding(Source.class)
public class Producer implements CommandLineRunner {

	@Autowired
	@Qualifier(Source.OUTPUT)
	private MessageChannel output;

	@Override
	public void run(String... args) throws Exception {
		output.send(MessageBuilder.withPayload("this is a message").build());
	}
}
