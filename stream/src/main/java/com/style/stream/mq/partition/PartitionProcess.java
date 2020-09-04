package com.style.stream.mq.partition;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author leon
 * @date 2020-09-03 15:20:25
 */
public interface PartitionProcess {
	String PARTITION_OUTPUT = "partitionOutput";
	String PARTITION_INPUT = "partitionInput";


	/**
	 * 分区 消息生产者
	 *
	 * @return MessageChannel
	 */
	@Output(PARTITION_OUTPUT)
	MessageChannel partitionOutput();


	/**
	 * 分区 消息消费者
	 *
	 * @return SubscribableChannel
	 */
	@Input(PARTITION_INPUT)
	SubscribableChannel partitionInput();
}
