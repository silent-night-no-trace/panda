package com.style.bus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * bus 启动类
 *
 * @author leon
 * @date 2020-09-07 10:30:06
 */
@SpringBootApplication
@EnableDiscoveryClient
public class BusApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusApplication.class);
	}
}
