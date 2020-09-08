package com.style.bus.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leon
 * @date 2020-09-07 14:08:53
 */
@RestController
@RefreshScope
public class TestController {

	@Value("${your.name}")
	private String name;

	@GetMapping("/getName")
	public String getName(){
		return "your name is "+name;
	}
}
