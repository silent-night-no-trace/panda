package com.style.stream.mq.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author leon
 * @date 2020-09-03 14:22:41
 */

@EnableBinding(GroupProcess.class)
public class GroupProducer implements CommandLineRunner {
	
	@Autowired
	@Qualifier(GroupProcess.GROUP_OUTPUT)
	private MessageChannel groupOutput;

	@Override
	public void run(String... args) throws Exception {
		groupOutput.send(MessageBuilder.withPayload("group message").build());
	}
}
