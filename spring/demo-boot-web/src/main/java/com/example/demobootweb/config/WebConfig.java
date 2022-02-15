package com.example.demobootweb.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/mobile/**")
					.addResourceLocations("classpath:/static/mobile/")
			.setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES))
			.resourceChain(true);
	}
}
