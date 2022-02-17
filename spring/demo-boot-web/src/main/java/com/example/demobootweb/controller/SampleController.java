package com.example.demobootweb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demobootweb.domain.Person;
import com.example.demobootweb.domain.Product;

@RestController
public class SampleController {

	@GetMapping("/hello/{name}")
	public String hello(@PathVariable("name") Person person) {
		return "hello " + person.getName();
	}

	@GetMapping("/hello")
	public String helloRequestParam(@RequestParam("name") Person person) {
		return "hello " + person.getName();
	}

	@GetMapping("/helloId")
	public String helloRequestParamId(@RequestParam("id") Product product) {
		return "hello " + product.getName();
	}

	@GetMapping("/message")
	public String stringMessage(@RequestBody String message) {
		return message;
	}

	@GetMapping(value = "/jsonMessage")
	public Person jsonMessage(@RequestBody Person jsonMessage) {
		return jsonMessage;
	}

	@GetMapping(value = "/xmlMessage")
	public Person xmlMessage(@RequestBody Person xmlMessage) {
		return xmlMessage;
	}
}
