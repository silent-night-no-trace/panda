package com.style.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * token 存储相关配置
 *
 * @author leon
 * @date 2020-09-17 17:40:31
 */
@Configuration
public class TokenConfig {

	/**
	 * 验证使用的key
	 */
	private static final String KEY = "MyStyle";

	/**
	 * jwt 存储
	 * Persistence interface for OAuth2 tokens.
	 *
	 * @return TokenStore
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	/**
	 * JwtAccessTokenConverter
	 *
	 * @return JwtAccessTokenConverter
	 */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		//对称秘钥，资源服务器使用该秘钥来验证
		jwtAccessTokenConverter.setSigningKey(KEY);
		return jwtAccessTokenConverter;
	}
}
