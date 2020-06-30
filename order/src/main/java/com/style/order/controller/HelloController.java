package com.style.order.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author leon
 * @date 2020-05-22 16:47:36
 */
@RestController
public class HelloController {

	@Value("${server.port}")
	private Integer port;

//	@Value("${your.password}")
//	private String password;

	@GetMapping("/sayHello")
	@HystrixCommand(fallbackMethod = "fallback")
	public String sayHello() {
		//System.out.println("password: "+password);
		return "hello style " + port;
	}

	public String fallback() {
		return "this is fallback";
	}
}
