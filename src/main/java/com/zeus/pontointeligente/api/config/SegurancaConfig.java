
package com.zeus.pontointeligente.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SegurancaConfig extends WebSecurityConfigurerAdapter {

	private static final String SENHA = "123";
	private static final String USUARIO = "cesar";
	private static final String ROLE = "ADMIN";

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(USUARIO).password(SENHA).roles(ROLE);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

//		http.authorizeRequests().anyRequest()//antMatchers(HttpMethod.POST,"/api/**")
//								//.hasRole(ROLE).antMatchers(HttpMethod.GET,"/api/**")
//								.hasAnyRole(ROLE).and().httpBasic().and().sessionManagement()
//								.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//								.csrf()
//								.disable();
		
		http.authorizeRequests().antMatchers(HttpMethod.POST,
				"/api/**").hasRole(ROLE)
		 .antMatchers(HttpMethod.GET,
						"/api/**")
				.hasAnyRole(ROLE).and().httpBasic().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.csrf()
				.disable();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
