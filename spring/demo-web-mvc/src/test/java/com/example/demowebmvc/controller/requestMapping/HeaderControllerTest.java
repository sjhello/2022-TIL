package com.example.demowebmvc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class HeaderControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void header() throws Exception {
		/*
		* /header 요청은 Accept Header 정보가 들어있는 클라이언트의 요청을 처리하는데 이 테스트는 성공한다
		* */

		this.mockMvc.perform(get("/header")
				.header(HttpHeaders.AUTHORIZATION, "111"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void authorizationSuccess() throws Exception {
		this.mockMvc.perform(get("/authorization")
				.header(HttpHeaders.AUTHORIZATION, "any"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void authorizationFail() throws Exception {
		this.mockMvc.perform(get("/authorization")
				.header(HttpHeaders.FROM, "sjhello"))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	void notAuthorizationSuccess() throws Exception {
		this.mockMvc.perform(get("/notAuthorization")
				.header(HttpHeaders.FROM, "sjhello"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void notAuthorizationFail() throws Exception {
		this.mockMvc.perform(get("/notAuthorization")
				.header(HttpHeaders.AUTHORIZATION, "sjhello"))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	void authorizationFillValueSuccess() throws Exception {
		this.mockMvc.perform(get("/authorizationFillValue")
				.header(HttpHeaders.AUTHORIZATION, "sjhello"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void authorizationFillValueFail() throws Exception {
		this.mockMvc.perform(get("/authorizationFillValue")
				.header(HttpHeaders.AUTHORIZATION, "1111"))
			.andDo(print())
			.andExpect(status().isNotFound());
	}
}
