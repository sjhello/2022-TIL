package com.example.bootnotusejsp.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Event {
	private String name;
	private LocalDateTime starts;

}
