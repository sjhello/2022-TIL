package com.example.demowebmvc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
class ContentTypeControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void jsonHello() throws Exception {
		this.mockMvc.perform(get("/jsonHello")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void failJsonHello() throws Exception {
		// 415 Unsupported Media Type

		this.mockMvc.perform(get("/jsonHello")
				.contentType(MediaType.APPLICATION_ATOM_XML_VALUE))
			.andDo(print())
			.andExpect(status().isUnsupportedMediaType());
	}

	@Test
	void produceJson() throws Exception {
		this.mockMvc.perform(get("/produceJson")
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void consumesProduceFail() throws Exception {
		// 406 Not Acceptable

		this.mockMvc.perform(get("/consumes-produce")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void consumesProduce() throws Exception {
		// 서버에서 produce 타입과 클라이언트의 accept 헤더가 달라도 성공하는 테스트

		this.mockMvc.perform(get("/consumes-produce")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
