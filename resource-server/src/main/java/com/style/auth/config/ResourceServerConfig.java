package com.style.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.HashMap;

/**
 * @author leon
 * @date 2020-06-18 16:59:28
 */
@EnableResourceServer
@Configuration
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
	public RemoteTokenServices tokenService(){
		RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
		remoteTokenServices.setClientId("user");
		remoteTokenServices.setClientSecret("style");
		remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:20110/oauth/check_token");
		return remoteTokenServices;
	}

//
//	@Bean
//	@Primary
//	public DefaultTokenServices tokenServices() {
//		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//		defaultTokenServices.setTokenStore(tokenStore());
//		return defaultTokenServices;
//	}

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
	 * Helper that translates between JWT encoded token values and OAuth authentication
	 * information (in both directions). Also acts as a {@link TokenEnhancer} when tokens are
	 * granted.
	 *
	 * @return JwtAccessTokenConverter
	 */
	@Bean
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
				headers.put("kid",KeyConfig.VERIFIER_KEY_ID);
				return JwtHelper.encode(content, rsaSigner,headers).getEncoded();
			}
		};
		converter.setSigner(rsaSigner);
		converter.setVerifier(new RsaVerifier(KeyConfig.getVerifierKey()));
		return converter;
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
}
