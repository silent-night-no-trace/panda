package com.style.gateway;

import com.style.gateway.filter.RequestTimeFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * 网关
 *
 * @author leon
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
		// @formatter:off
		return builder.routes()
				.route(r -> r.path("/customer/**")
						.filters(f -> f.filter(new RequestTimeFilter())
								.addResponseHeader("X-Response-Default-Foo", "Default-Bar"))
						.uri("http://180.101.49.11")
						.order(0)
						.id("customer_filter_router")
				)
				.build();
		// @formatter:on
	}

}
//	@Bean
//	public RouteLocator custom(RouteLocatorBuilder builder){
//		//@formatter:off
//		return builder.routes()
//				.route("path_route", r -> r.path("/get")
//						.uri("http://httpbin.org"))
//				.route("host_route", r -> r.host("*.myhost.org")
//						.uri("http://httpbin.org"))
//				.route("rewrite_route", r -> r.host("*.rewrite.org")
//						.filters(f -> f.rewritePath("/foo/(?<segment>.*)",
//								"/${segment}"))
//						.uri("http://httpbin.org"))
//				.route("hystrix_route", r -> r.host("*.hystrix.org")
//						.filters(f -> f.hystrix(c -> c.setName("slow-cmd")))
//						.uri("http://httpbin.org"))
//				.route("hystrix_fallback_route", r -> r.host("*.hystrixfallback.org")
//						.filters(f -> f.hystrix(c -> c.setName("slow-cmd").setFallbackUri("forward:/hystrixfallback")))
//						.uri("http://httpbin.org"))
////				.route("limit_route", r -> r
////						.host("*.limited.org").and().path("/anything/**")
////						.filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
////						.uri("http://httpbin.org"))
//				.route("websocket_route", r -> r.path("/echo")
//						.uri("ws://localhost:9000"))
//				.build();
//	}}
