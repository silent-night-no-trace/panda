package com.style.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

/**
 * 认证配置
 *
 * @author leon
 * @date 2020-06-17 17:06:30
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	private final AuthenticationManager authenticationManager;

	private final ClientDetailsService clientDetailsService;

	private final UserDetailsService userDetailsService;

	@Autowired
	public AuthorizationServerConfig(ClientDetailsService clientDetailsService, AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
		this.clientDetailsService = clientDetailsService;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
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
	 * @param endpoints endpoints
	 * @throws Exception endpoints
	 */
	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		//用来配置授权以及令牌（Token）的访问端点和令牌服务（比如：配置令牌的签名与存储方式）
		endpoints
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
				.authenticationManager(this.authenticationManager)
				//token 存储
				.tokenStore(tokenStore())
				.userDetailsService(userDetailsService)
		;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//内存中配置客户端信息
		//用来配置客户端详情信息，一般使用数据库来存储或读取应用配置的详情信息
		clients.inMemory().withClient("user")
				.accessTokenValiditySeconds(30 * 60)
				.refreshTokenValiditySeconds(30 * 60)
				.authorizedGrantTypes("authorization_code", "refresh_token", "client_credentials", "password","implicit")
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
				.secret("{noop}style")
				.redirectUris("https://www.baidu.com")
		;
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
