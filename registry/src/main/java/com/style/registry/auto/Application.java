package com.style.registry.auto;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author leon
 * @date 2020-07-13 16:56:57
 */
@EnableCustomBean
public class Application {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
		CustomBean bean = context.getBean(CustomBean.class);
		System.out.println(bean.getId()+" "+bean.getName());
	}
}
