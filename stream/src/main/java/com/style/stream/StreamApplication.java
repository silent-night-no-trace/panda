package com.style.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * stream项目启动类
 *
 * @author leon
 * @date 2020-07-09 15:41:09
 */
@SpringBootApplication
@EnableDiscoveryClient
public class StreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamApplication.class);
	}
}
