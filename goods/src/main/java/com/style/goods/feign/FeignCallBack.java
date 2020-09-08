package com.style.goods.feign;

import org.springframework.stereotype.Component;

/**
 * @author leon
 * @date 2020-08-19 16:03:24
 */
@Component
public class FeignCallBack implements OrderFeign{

	@Override
	public String sayHello() {
		return "系统有点拥挤,请稍后访问";
	}
}
