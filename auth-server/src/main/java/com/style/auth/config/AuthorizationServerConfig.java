package com.style.auth.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import java.util.HashMap;

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

		//.userApprovalHandler(userApprovalHandler())
		//.accessTokenConverter(accessTokenConverter());
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
	 * Basic interface for determining whether a given client authentication request has been
	 * approved by the current user.
	 *
	 * @return UserApprovalHandler
	 */
	@Bean
	public UserApprovalHandler userApprovalHandler() {
		TokenStoreUserApprovalHandler tokenStoreUserApprovalHandler = new TokenStoreUserApprovalHandler();
		tokenStoreUserApprovalHandler.setClientDetailsService(clientDetailsService);
		tokenStoreUserApprovalHandler.setTokenStore(tokenStore());
		tokenStoreUserApprovalHandler.setRequestFactory(oAuth2RequestFactory());
		return tokenStoreUserApprovalHandler;
	}

	@Bean
	public OAuth2RequestFactory oAuth2RequestFactory() {
		return new DefaultOAuth2RequestFactory(clientDetailsService);
	}

	/**
	 * Persistence interface for OAuth2 tokens.
	 *
	 * @return TokenStore
	 */
	@Bean
	public TokenStore tokenStore() {
//		JwtTokenStore tokenStore = new JwtTokenStore(accessTokenConverter());
//		tokenStore.setApprovalStore(approvalStore());

		return new InMemoryTokenStore();
	}

	/**
	 * * Interface for saving, retrieving and revoking user approvals (per client, per scope).
	 *
	 * @return ApprovalStore
	 */
	@Bean
	public ApprovalStore approvalStore() {
		return new InMemoryApprovalStore();
	}

	/**
	 * Helper that translates between JWT encoded token values and OAuth authentication
	 * information (in both directions). Also acts as a {@link TokenEnhancer} when tokens are
	 * granted.
	 *
	 * @return JwtAccessTokenConverter
	 */
	//@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		RsaSigner rsaSigner = new RsaSigner(KeyConfig.getSignerKey());
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
			private final JsonParser objectMapper = JsonParserFactory.create();

			@Override
			protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
				String content;
				try {
					content = this.objectMapper.formatMap(getAccessTokenConverter().convertAccessToken(accessToken, authentication));
				} catch (Exception ex) {
					throw new IllegalStateException("Cannot convert access token to JSON", ex);
				}
				HashMap<String, String> headers = new HashMap<>(16);
				headers.put("kid", KeyConfig.VERIFIER_KEY_ID);
				return JwtHelper.encode(content, rsaSigner, headers).getEncoded();
			}
		};
		converter.setSigner(rsaSigner);
		converter.setVerifier(new RsaVerifier(KeyConfig.getVerifierKey()));
		return converter;
	}

	@Bean
	public JWKSet jwkSet() {
		RSAKey.Builder builder = new RSAKey.Builder(KeyConfig.getVerifierKey())
				.keyUse(KeyUse.SIGNATURE)
				.algorithm(JWSAlgorithm.RS256)
				.keyID(KeyConfig.VERIFIER_KEY_ID);
		return new JWKSet(builder.build());
	}
}
