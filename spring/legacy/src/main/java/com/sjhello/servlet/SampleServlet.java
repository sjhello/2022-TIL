package com.sjhello.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SampleServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		System.out.println("SampleServlet init");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Object name = getServletContext().getAttribute("name");
		System.out.println("SampleServlet");
		System.out.println(name);
	}

	@Override
	public void destroy() {
		System.out.println("SampleServlet destroy");
	}
}
