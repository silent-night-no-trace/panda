package com.style.stream.mq.group;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

/**
 * @author leon
 * @date 2020-09-03 14:20:51
 */
@EnableBinding(GroupProcess.class)
public class GroupConsumer2 {

	@StreamListener(GroupProcess.GROUP_INPUT2)
	public void receive(Message<String> message){
		System.out.println("group2 receive:"+message.getPayload());
	}
}
