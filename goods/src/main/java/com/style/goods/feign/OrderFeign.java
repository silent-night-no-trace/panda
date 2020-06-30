package com.style.goods.feign;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author leon
 * @date 2020-05-22 23:20:22
 */
@FeignClient(value = "order")
public interface OrderFeign {

	/**
	 * sayHello
	 *
	 * @return String
	 */
	@GetMapping(value = "/sayHello")
	@LoadBalanced()
	String sayHello();
}
