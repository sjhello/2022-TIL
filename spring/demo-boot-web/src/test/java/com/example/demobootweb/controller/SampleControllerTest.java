package com.example.demobootweb.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demobootweb.domain.Person;
import com.example.demobootweb.domain.Product;
import com.example.demobootweb.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class SampleControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void hello() throws Exception {
		this.mockMvc.perform(get("/hello/sjhello"))
			.andDo(print())
			.andExpect(content().string("hello sjhello"));
	}

	@Test
	void hellorRequestParam() throws Exception {
		this.mockMvc.perform(get("/hello")
				.param("name", "sjhello"))
			.andDo(print())
			.andExpect(content().string("hello sjhello"));
	}

	@Test
	void helloRequestParamId() throws Exception {
		Product product = new Product();
		product.setName("computer");
		Product computer = productRepository.save(product);

		this.mockMvc.perform(get("/helloId")
				.param("id", computer.getId().toString()))
			.andDo(print())
			.andExpect(content().string("hello computer"));
	}

	@Test
	void helloResourceHandler() throws Exception {
		this.mockMvc.perform(get("/index.html"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(Matchers.containsString("hello ResourceHandler")));
	}

	@Test
	void helloMobileResourceHandler() throws Exception {
		this.mockMvc.perform(get("/mobile/index.html"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(Matchers.containsString("Hello Mobile")))
			.andExpect(header().exists(HttpHeaders.CACHE_CONTROL));
	}

	@Test
	void stringMessage() throws Exception {
		this.mockMvc.perform(get("/message")
				.contentType(MediaType.APPLICATION_JSON)
				.content("hello"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string("hello"));
	}

	@Test
	void jsonMessage() throws Exception {
		Person person = new Person();
		person.setId(1L);
		person.setName("sjhello");

		this.mockMvc.perform(get("/jsonMessage")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(person)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value("1"))
			.andExpect(jsonPath("$.name").value("sjhello"));
	}
}
