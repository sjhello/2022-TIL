package com.example.demowebmvc.controller.handlerMethod;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demowebmvc.controller.handlerMethod.domain.Event;

@RestController
public class UriPatternsController {

	@GetMapping("/event/{id}")
	public Event event(@PathVariable Integer id) {
		Event event = new Event();
		event.setId(id);

		return event;
	}
}
