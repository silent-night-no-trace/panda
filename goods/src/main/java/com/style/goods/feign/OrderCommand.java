package com.style.goods.feign;

import com.netflix.hystrix.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author leon
 * @date 2020-08-15 11:06:00
 */
public class OrderCommand extends HystrixCommand<String> {
	private final RestTemplate restTemplate;
	private final Long id;

	public OrderCommand(RestTemplate restTemplate, Long id) {
		super(setter());
		this.restTemplate = restTemplate;
		this.id = id;
	}

	private static Setter setter() {
		// 服务分组
		HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("shop_product");
		// 服务标识
		final HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("shop");
		//服务线程池
		final HystrixThreadPoolKey threadPoolKey = HystrixThreadPoolKey.Factory.asKey("shop_product_pool");

		HystrixThreadPoolProperties.Setter threadPoolProperties = HystrixThreadPoolProperties.Setter().withCoreSize(5)
				//线程存活时间15秒
				.withKeepAliveTimeMinutes(15)
				// 队列等待的阈值为100,超过100执行拒绝
				.withQueueSizeRejectionThreshold(1000);
		HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
				// 采用线程池方式实现服务隔离
				.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
				.withExecutionTimeoutEnabled(false)
				.withExecutionTimeoutInMilliseconds(1);

		return Setter.withGroupKey(groupKey)
				.andCommandKey(commandKey)
				.andCommandPropertiesDefaults(commandProperties)
				.andThreadPoolKey(threadPoolKey)
				.andThreadPoolPropertiesDefaults(threadPoolProperties)
				;
	}

	@Override
	protected String run() throws Exception {
		//请求地址
		return restTemplate.getForObject("http://goods/get/"+id, String.class);
	}

	@Override
	protected String getFallback() {
		return "熔断降级";
	}
}
