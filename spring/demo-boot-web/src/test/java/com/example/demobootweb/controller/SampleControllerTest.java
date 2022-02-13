package com.example.demobootweb.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class SampleControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void hello() throws Exception {
		this.mockMvc.perform(get("/hello/sjhello"))
			.andDo(print())
			.andExpect(content().string("hello sjhello"));
	}

	@Test
	void hellorRequestParam() throws Exception {
		this.mockMvc.perform(get("/hello?name=sjhello"))
			.andDo(print())
			.andExpect(content().string("hello sjhello"));
	}
}
