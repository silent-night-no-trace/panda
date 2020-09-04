//package com.style.stream.kafka;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.messaging.Sink;
//import org.springframework.cloud.stream.messaging.Source;
//import org.springframework.integration.support.MessageBuilder;
//
///**
// * @author leon
// * @date 2020-07-10 16:17:20
// */
//@EnableBinding({Sink.class, Source.class})
//public class Sample implements CommandLineRunner {
//
//	@Autowired
//	private Source source;
//
//	@Override
//	public void run(String... args) throws Exception {
//		source.output().send(MessageBuilder.withPayload("this is message ").build());
//	}
//}
