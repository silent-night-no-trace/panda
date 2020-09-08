package com.style.order.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leon
 * @date 2020-05-22 16:47:36
 */
@RestController
@Slf4j
public class HelloController {

	@Value("${server.port}")
	private Integer port;

	/**
	 * 位于配置中心的配置
	 */
	@Value("${your.password}")
	private String password;

	@GetMapping("/sayHello")
	@HystrixCommand(fallbackMethod = "fallback")
	public String sayHello() {
		return "hello style " + port;
	}

	public String fallback() {
		return "this is fallback";
	}


	@GetMapping("/getPassword")
	public String getPassword() {
		return "your password is " + password;
	}
}
