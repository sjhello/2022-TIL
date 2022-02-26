package com.example.demowebmvc.controller.requestMapping;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class RequestParamControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void paramSuccess() throws Exception {
		this.mockMvc.perform(get("/param")
				.param("name", "sjhello"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void paramFail() throws Exception {
		this.mockMvc.perform(get("/param"))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@Test
	void paramNotExistNameSuccess() throws Exception {
		this.mockMvc.perform(get("/paramNotExistName")
				.param("key", "key"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void paramNotExistNameFail() throws Exception {
		this.mockMvc.perform(get("/paramNotExistName")
				.param("name", "key"))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}
}
