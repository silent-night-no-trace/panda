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
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.HashMap;
import java.util.Map;

/**
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
	public UserDetailsService userDetails(){
		UserDetails admin = User.withUsername("admin")
				.password("{bcrypt}$2a$10$ZlFDDZMkZ9P7Yb4BsZ50ZueNzn7yM3GTJD97M5cJMWDu4oKr1Lsuq")
				.roles("ADMIN","USER")
				.build();
		UserDetails user = User.withUsername("user")
				.password("{bcrypt}"+new BCryptPasswordEncoder().encode("123456"))
				.roles("USER")
				.build();
		//内存用户管理器
		//JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
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

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new DelegatingPasswordEncoder("noop",encoders);
//	}

	private static final Map<String,PasswordEncoder> encoders = new HashMap<>(4);

	static {
		encoders.put("bcrypt", new BCryptPasswordEncoder());
		encoders.put("noop", NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
	}

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("123456"));
	}
}
