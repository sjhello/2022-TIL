package io.security.corespringsecurity.security.configs;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private String plainText = "1234";

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		String password = passwordEncoder().encode(plainText);

		auth.inMemoryAuthentication().withUser("user").password(password).roles("USER");
		auth.inMemoryAuthentication().withUser("manager").password(password).roles("MANAGER", "USER");
		auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN","USER","MANAGER");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers("/mypage").hasAnyRole("USER", "MANAGER", "ADMIN")
			.antMatchers("/messages").hasAnyRole("MANAGER", "ADMIN")
			.antMatchers("/config").hasAnyRole("ADMIN")
			.anyRequest().authenticated()
		.and()
			.formLogin();
	}

	/*
	* permitAll()은 보안 필터를 거친 후에 해당 요청에 대해서 승인을 한 것
	* ignoring()은 보안 필터를 거치지 않음
	* */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
}
