package com.sample.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sample.domain.Event;

@Service
public class EventService {



	public List<Event> getEvents() {
		List<Event> events = new ArrayList<>();
		events.add(Event.builder()
				.name("뮤지컬")
				.limitOfEnrollment(40)
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.of(2022, 3, 20, 22, 00))
			.build());
		events.add(Event.builder()
			.name("락 페스티벌")
			.limitOfEnrollment(60)
			.startTime(LocalDateTime.now())
			.endTime(LocalDateTime.of(2022, 4, 20, 23, 00))
			.build());

		return events;
	}
}
