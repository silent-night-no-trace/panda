package com.style.stream.mq.partition;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

/**
 * @author leon
 * @date 2020-09-03 16:22:54
 */
@EnableBinding(PartitionProcess.class)
public class PartitionConsumer {

	@StreamListener(PartitionProcess.PARTITION_INPUT)
	public void receive(Message<String> message){
		System.out.println("receive partition message : "+message.getPayload());
	}
}
