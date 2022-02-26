package com.example.demowebmvc.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeaderController {

	@GetMapping(value = "/header", headers = HttpHeaders.ACCEPT)
	public String header() {
		return "header";
	}

	@GetMapping(value = "/authorization", headers = HttpHeaders.AUTHORIZATION)
	public String authorization() {
		return "authorization";
	}

	@GetMapping(value = "/notAuthorization", headers = "!" + HttpHeaders.AUTHORIZATION)
	public String notAuthorization() {
		return "notAuthorization";
	}

	@GetMapping(value = "/authorizationFillValue", headers = HttpHeaders.AUTHORIZATION + "=" + "sjhello")
	public String authorizationFillValue() {
		return "authorizationFillValue";
	}
}
