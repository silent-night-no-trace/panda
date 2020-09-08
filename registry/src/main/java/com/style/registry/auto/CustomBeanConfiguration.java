package com.style.registry.auto;

import org.springframework.context.annotation.Bean;

/**
 * @author leon
 * @date 2020-07-13 16:51:09
 */
public class CustomBeanConfiguration {

	@Bean
	public CustomBean customBean(){
		return new CustomBean(1,"name");
	}
}
