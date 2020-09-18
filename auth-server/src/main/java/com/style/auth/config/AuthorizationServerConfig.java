package com.style.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import java.util.Collections;

/**
 * 认证配置
 *
 * @author leon
 * @date 2020-06-17 17:06:30
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private JwtAccessTokenConverter jwtAccessTokenConverter;

//	@Autowired
//	public AuthorizationServerConfig(AuthenticationManager authenticationManager, UserDetailsService userDetails) {
//		this.authenticationManager = authenticationManager;
//	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		//用来配置令牌端点(Token Endpoint)的安全与权限访问。
		security
				.tokenKeyAccess("permitAll()")
				.checkTokenAccess("permitAll()")
				.accessDeniedHandler(new AccessDeniedHandlerImpl())
				//允许表单传入 client_id client_secret进行认证
				.allowFormAuthenticationForClients()
		;
	}

	/**
	 * 认证配置
	 *
	 * @param endpoints endpoints
	 */
	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
		//用来配置授权以及令牌（Token）的访问端点和令牌服务（比如：配置令牌的签名与存储方式）
		endpoints
				//允许通过的方法
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
				//认证管理器
				.authenticationManager(this.authenticationManager)
				//toke服务
				.tokenServices(tokenServices())
				//授权码服务
				.authorizationCodeServices(authorizationCodeServices())
		;
	}

	@Bean
	public AuthorizationServerTokenServices tokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		//客户端详情
		tokenServices.setClientDetailsService(clientDetailsService);
		//是否支持刷新token
		tokenServices.setSupportRefreshToken(true);
		//令牌存储策略
		tokenServices.setTokenStore(tokenStore);
		//令牌增强
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Collections.singletonList(jwtAccessTokenConverter));
		tokenServices.setTokenEnhancer(tokenEnhancerChain);
		// 令牌默认有效期2小时
		tokenServices.setAccessTokenValiditySeconds(7200);
		// 刷新令牌默认有效期3天
		tokenServices.setRefreshTokenValiditySeconds(259200);
		return tokenServices;
	}

	/**
	 * 授权码服务
	 *
	 * @return AuthorizationCodeServices
	 */
	@Bean
	public AuthorizationCodeServices authorizationCodeServices() {
		//采用内存存储
		return new InMemoryAuthorizationCodeServices();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//内存中配置客户端信息
		//用来配置客户端详情信息，一般使用数据库来存储或读取应用配置的详情信息
		clients.inMemory().withClient("user")
				//访问码默认有效时间 2个小时
				.accessTokenValiditySeconds(120 * 60)
				//刷新默认有效时间 2天
				.refreshTokenValiditySeconds(2 * 24 * 60 * 60)
				.authorizedGrantTypes("authorization_code", "refresh_token", "client_credentials", "password", "implicit")
				.scopes("all", "read", "write")
				// // secret密码配置从 Spring Security 5.0开始必须以 {加密方式}+加密后的密码 这种格式填写
				//        /*
				//         *   当前版本5新增支持加密方式：
				//         *   bcrypt - BCryptPasswordEncoder (Also used for encoding)
				//         *   ldap - LdapShaPasswordEncoder
				//         *   MD4 - Md4PasswordEncoder
				//         *   MD5 - new MessageDigestPasswordEncoder("MD5")
				//         *   noop - NoOpPasswordEncoder
				//         *   pbkdf2 - Pbkdf2PasswordEncoder
				//         *   scrypt - SCryptPasswordEncoder
				//         *   SHA-1 - new MessageDigestPasswordEncoder("SHA-1")
				//         *   SHA-256 - new MessageDigestPasswordEncoder("SHA-256")
				//         *   sha256 - StandardPasswordEncoder
				//         */
				.secret(new BCryptPasswordEncoder().encode("style"))
				.redirectUris("https://www.baidu.com")
		;
	}

	/**
	 * 指定加密方式
	 *
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
