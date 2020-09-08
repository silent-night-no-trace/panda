package com.style.goods.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.style.goods.feign.OrderCommand;
import com.style.goods.feign.OrderFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author leon
 * @date 2020-05-22 23:22:05
 */
@RestController
public class GoodsController {

	@Autowired
	@Qualifier("order")
	private OrderFeign orderFeign;

	@GetMapping("/sayHello")
	@HystrixCommand(fallbackMethod = "fallback")
	public String sayHello(){
		return orderFeign.sayHello();
	}

	public String fallback(){
		return "this is fallback";
	}
}
