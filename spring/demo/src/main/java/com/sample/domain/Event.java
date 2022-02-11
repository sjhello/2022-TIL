package com.sample.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Event {
	private String name;
	private Integer limitOfEnrollment;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
}
