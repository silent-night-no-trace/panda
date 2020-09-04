package com.style.stream.integration;

import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.SubscribableChannel;

import java.util.concurrent.*;

/**
 * spring integration 使用
 *
 * @author leon
 * @date 2020-07-10 11:05:45
 */
public class IntegrationUse {

	private static final Integer CORE_AMOUNT = Runtime.getRuntime().availableProcessors()*2+1;

	private static final ThreadPoolExecutor TS =new ThreadPoolExecutor(CORE_AMOUNT, CORE_AMOUNT, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024), r -> {
		Thread thread = new Thread(r);
		thread.setName("ts-channel");
		return thread;
	});
	
	public static void main(String[] args) {
		//消息通道
		SubscribableChannel messageChannel =new DirectChannel();
		//订阅接收到的消息
		messageChannel.subscribe(msg-> {
			System.out.println("receive: " +msg.getPayload());
		});
		//向通道发送消息
		messageChannel.send(MessageBuilder.withPayload("this is message").build());

		// DirectChannel 中 存在  UnicastingDispatcher ，会分发到对应的消息通道 MessageChannel 中，
		// 从名字也可以看出来，UnicastingDispatcher 是个单播的分发器，只能选择一个消息通道。
		// 那么如何选择呢? 内部提供了 LoadBalancingStrategy 负载均衡策略，默认只有轮询的实现，可以进行扩展。
		// 单播的消息分发器
		//消息通道
		SubscribableChannel messageChannelMulti =new DirectChannel();
		//订阅接收到的消息
		messageChannelMulti.subscribe(msg-> {
			System.out.println("receive: " +msg.getPayload());
		});

		messageChannelMulti.subscribe(msg-> {
			System.out.println("receive: " +msg.getPayload());
		});
		//向通道发送消息
		messageChannelMulti.send(MessageBuilder.withPayload("this is message").build());
		messageChannelMulti.send(MessageBuilder.withPayload("this is message2").build());

		//广播的消息分发器
		PublishSubscribeChannel publishSubscribeChannel = new PublishSubscribeChannel();
		publishSubscribeChannel.subscribe(message -> {
			System.out.println("publishSubscribeChannel receive: " +message.getPayload());
		});

		publishSubscribeChannel.subscribe(message -> {
			System.out.println("publishSubscribeChannel receive2: " +message.getPayload());
		});

		publishSubscribeChannel.send(MessageBuilder.withPayload("publishSubscribeChannel message one ").build());
		publishSubscribeChannel.send(MessageBuilder.withPayload("publishSubscribeChannel message two ").build());


		//消息通道
		ExecutorChannel executorChannel = new ExecutorChannel(TS);
		//订阅消息通道收到的消息
		executorChannel.subscribe(message -> {
			System.out.println("receive: "+message.getPayload() +" headers: "+ message.getHeaders());
		});
		//向通道发送消息
		executorChannel.send(MessageBuilder.withPayload("this is message to executorChannel").build());

	}
}
