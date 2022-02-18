package com.example.demowebmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {

	@RequestMapping("/sample")
	@ResponseBody
	public String sample() {
		return "sample";
	}
}
