package com.example.demobootweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/*
		* order 값은 작을수록 높은 우선순위를 가짐
		* */
		registry.addInterceptor(new HelloInterceptor()).addPathPatterns("/hello/**").order(1);
		registry.addInterceptor(new GoodbyeInterceptor()).excludePathPatterns("/hello/**").order(-1);
	}
}
