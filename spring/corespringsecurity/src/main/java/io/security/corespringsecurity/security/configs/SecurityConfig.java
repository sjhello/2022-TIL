package io.security.corespringsecurity.security.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

		auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN");
		auth.inMemoryAuthentication().withUser("manager").password(password).roles("MANAGER");
		auth.inMemoryAuthentication().withUser("user").password(password).roles("USER");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers("/messages").hasRole("MANAGER")
			.antMatchers("/mypage").hasRole("USER")
			.antMatchers("/config").hasRole("ADMIN")
			.anyRequest().authenticated()
		.and()
			.formLogin();
	}


}
