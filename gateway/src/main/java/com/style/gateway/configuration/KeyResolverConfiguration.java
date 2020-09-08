package com.style.gateway.configuration;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 限流
 *
 * @author leon
 * @date 2020-08-28 15:01:14
 */
@Configuration
public class KeyResolverConfiguration {

	/**
	 * 请求路径限流
	 *
	 * @return KeyResolver
	 */
	@Bean
	@Primary
	public KeyResolver pathKeyResolver() {
		return exchange -> Mono.just(exchange.getRequest().getPath().toString());
	}

	/**
	 * 请求路径限流
	 *
	 * @return KeyResolver
	 */
	@Bean
	public KeyResolver ipKeyResolver() {
		return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getHeaders().getFirst("X-Forwarded-For")));
	}

	/**
	 * 用户限流
	 *
	 * @return KeyResolver
	 */
	@Bean
	public KeyResolver userKeyResolver() {
		return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getHeaders().getFirst("token")));
	}
}
