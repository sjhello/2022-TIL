package com.sjhello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sjhello.service.HelloService;

@Controller
public class HelloController {

	@Autowired
	HelloService helloService;

	@GetMapping("/hello")
	@ResponseBody
	public String hello() {
		return helloService.hello();
	}

	@GetMapping("/sample")
	public void helloView() {

	}
}
