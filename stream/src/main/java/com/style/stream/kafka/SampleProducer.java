//package com.style.stream.sample;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.cloud.stream.messaging.Source;
//import org.springframework.integration.support.MessageBuilder;
//import org.springframework.stereotype.Component;
//
//
///**
// * 简单生产者
// *
// * @author leon
// * @date 2020-07-10 16:26:30
// */
//@Component
//public class SampleProducer implements CommandLineRunner {
//
//	private final Source source;
//
//	public SampleProducer(Source source) {
//		this.source = source;
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		source.output().send(MessageBuilder.withPayload("this is message").build());
//	}
//}
