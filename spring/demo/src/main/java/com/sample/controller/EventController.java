package com.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sample.service.EventService;

@Controller
public class EventController {

	@Autowired
	EventService eventService;

	@GetMapping("/events")
	public String events(Model model) {
		model.addAttribute("events", eventService.getEvents());
		return "events/list";
	}
}
