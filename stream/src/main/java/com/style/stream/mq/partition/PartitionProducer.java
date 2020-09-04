package com.style.stream.mq.partition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author leon
 * @date 2020-09-03 16:20:28
 */
@EnableBinding(PartitionProcess.class)
public class PartitionProducer implements CommandLineRunner {

	@Autowired
	@Qualifier(PartitionProcess.PARTITION_OUTPUT)
	private MessageChannel partitionOutput;

	@Override
	public void run(String... args) throws Exception {
		partitionOutput.send(MessageBuilder.withPayload("partition message").build());
	}
}
