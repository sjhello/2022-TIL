package com.sjhello;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebApplication implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// Root WebApplicationContext
		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
		applicationContext.setServletContext(servletContext);
		applicationContext.register(AppConfig.class);
		applicationContext.refresh();

		// Servlet WebApplicationContext
		DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
		dispatcherServlet.setContextClass(WebConfig.class);

		ServletRegistration.Dynamic app = servletContext.addServlet("app", dispatcherServlet);
		app.addMapping("/app/*");
	}
}
