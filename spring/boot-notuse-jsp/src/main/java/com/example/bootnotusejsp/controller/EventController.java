package com.example.bootnotusejsp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.bootnotusejsp.domain.Event;

@Controller
public class EventController {

	@GetMapping("/events")
	public String getEvents(Model model) {
		Event event1 = Event.builder()
			.name("이벤트1")
			.starts(LocalDateTime.now())
			.build();

		Event event2 = Event.builder()
			.name("이벤트2")
			.starts(LocalDateTime.now())
			.build();

		List<Event> events = List.of(event1, event2);

		model.addAttribute("events", events);
		return "events/list";
	}
}
