package com.style.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * 资源服务配置
 *
 * @author leon
 * @date 2020-06-18 16:59:28
 */
@EnableResourceServer
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "resource";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(RESOURCE_ID);
		resources.tokenServices(tokenService());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/oauth/**").permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.csrf()
				.disable()
				.formLogin()
		;
	}

	@Bean
	public RemoteTokenServices tokenService() {
		//远程token服务 即为认证的服务器的check_token地址
		RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
		remoteTokenServices.setClientId("user");
		remoteTokenServices.setClientSecret("style");
		remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:20110/oauth/check_token");
		return remoteTokenServices;
	}

	/**
	 * Persistence interface for OAuth2 tokens.
	 *
	 * @return TokenStore
	 */
	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}
}
