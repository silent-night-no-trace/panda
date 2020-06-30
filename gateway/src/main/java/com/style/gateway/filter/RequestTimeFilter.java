package com.style.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author leon
 * @date 2020-05-22 22:03:40
 */
public class RequestTimeFilter implements GatewayFilter {

	private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
		return chain.filter(exchange).then(
				Mono.fromRunnable(() -> {
					Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
					if (startTime != null) {
						System.out.println(exchange.getRequest().getURI().getRawPath() + ": " + (System.currentTimeMillis() - startTime) + "ms");
					}
				})
		);

	}
}
