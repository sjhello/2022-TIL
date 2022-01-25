package com.sjhello.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.sjhello.service.HelloService;

public class HelloServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		System.out.println("HelloServlet init");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		WebApplicationContext context = (WebApplicationContext)getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		HelloService bean = context.getBean(HelloService.class);
		System.out.println(bean.hello());

		String name = (String)getServletContext().getAttribute("name");
		System.out.println("HelloServlet doget");
		System.out.println(name);
	}

	@Override
	public void destroy() {
		System.out.println("HelloServlet destroy");
	}
}
