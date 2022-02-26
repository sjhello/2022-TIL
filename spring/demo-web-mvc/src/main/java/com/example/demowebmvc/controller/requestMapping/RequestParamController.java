package com.example.demowebmvc.controller.requestMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestParamController {

	@GetMapping(value = "/param", params = "name")
	public void param() {
	}

	@GetMapping(value = "/paramNotExistName", params = "!name")
	public void paramNotExistName() {
	}
}
