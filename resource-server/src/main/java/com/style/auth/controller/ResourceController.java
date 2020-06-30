package com.style.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 *
 * @author leon
 * @date 2020-06-30 14:06:53
 */
@RestController
public class ResourceController {

	@Value("${server.port}")
	private Integer serverPort;

	@GetMapping("/port")
	public String getPort() {
		return "server port is :" + serverPort;
	}
}
