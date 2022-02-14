package com.example.demobootweb.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demobootweb.domain.Person;
import com.example.demobootweb.domain.Product;
import com.example.demobootweb.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
class SampleControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ProductRepository productRepository;

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
}
