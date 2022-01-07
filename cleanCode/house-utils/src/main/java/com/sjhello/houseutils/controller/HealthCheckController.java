package com.sjhello.houseutils.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author sjhello
 *
 * 서비스 HealthCheck Controller
 * */
@RestController
public class HealthCheckController {

	@GetMapping("/api/ping")
	public String ping() {
		return "ok";
	}
}
