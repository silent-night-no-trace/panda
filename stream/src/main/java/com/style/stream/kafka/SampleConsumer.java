//package com.style.stream.sample;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.stream.annotation.StreamListener;
//import org.springframework.cloud.stream.messaging.Sink;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
///**
// * @author leon
// * @date 2020-07-10 16:28:53
// */
//@Component
//public class SampleConsumer {
//
//	@StreamListener(value = Sink.INPUT)
//	@KafkaListener(id = "demo-group")
//	public void messageHandler(String message){
//		System.out.println("receive: "+message);
//	}
//}
