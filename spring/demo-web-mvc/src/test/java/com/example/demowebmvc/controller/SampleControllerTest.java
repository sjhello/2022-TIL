package com.example.demowebmvc.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class SampleControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void sample() throws Exception {
		mockMvc.perform(get("/sample"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string("sample"));
	}

	@Test
	void sampleOptions() throws Exception {
		mockMvc.perform(options("/sample"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().stringValues(HttpHeaders.ALLOW, hasItems(containsString("POST"))));
	}

	@Test
	void sampleOptionsFail() throws Exception {
		mockMvc.perform(options("/sample"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().stringValues(HttpHeaders.ALLOW, "GET","POST"));
	}
}
