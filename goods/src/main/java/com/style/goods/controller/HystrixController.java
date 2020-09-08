package com.style.goods.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.style.goods.feign.OrderCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author leon
 * @date 2020-08-21 15:10:41
 */
@RestController
public class HystrixController {

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/buy/{id}")
	@HystrixCommand(fallbackMethod = "fall")
	public String find(@PathVariable("id") Long id) {
		return new OrderCommand(restTemplate, id).execute();
	}

	/**
	 * 熔断方法
	 *
	 * @param id id
	 * @return String
	 */
	public String fall(Long id) {
		return "道路太拥挤";
	}

	@GetMapping("/get/{id}")
	@HystrixCommand(fallbackMethod = "fall", commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
	})
	public String get(@PathVariable("id") Long id) {

		if (id == 1) {
			throw new RuntimeException("太忙了");
		}
		return "天呐撸";
	}
}
