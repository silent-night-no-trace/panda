package com.style.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Security 配置
 *
 * @author leon
 * @date 2020-06-18 15:23:04
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS).permitAll()
				.antMatchers("/oauth/**").permitAll()
				.anyRequest().authenticated()
				.and()
				//basic 弹窗登录
				//.httpBasic()
				//表单登录
				.formLogin()
				.and()
				.csrf().disable()
		;
	}

	@Bean
	public UserDetailsService userDetails() {
		UserDetails admin = User.withUsername("admin")
				//,默认BCryptPasswordEncoder 更多实现 org.springframework.security.crypto.password.PasswordEncoder
				//可查看该接口的实现
				// password  Spring Security 5.0开始必须以 {加密方式}+加密后的密码 这种格式填写
				.password(new BCryptPasswordEncoder().encode("123456"))
				.roles("ADMIN", "USER")
				.build();
		UserDetails user = User.withUsername("user")
				.password(new BCryptPasswordEncoder().encode("123456"))
				.roles("USER")
				.build();
		//内存用户管理器
		InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
		userDetailsManager.createUser(admin);
		userDetailsManager.createUser(user);
		return userDetailsManager;
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

}
