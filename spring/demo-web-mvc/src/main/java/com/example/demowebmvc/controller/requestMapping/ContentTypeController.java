package com.example.demowebmvc.controller.requestMapping;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentTypeController {

	@RequestMapping(value = "/jsonHello", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String jsonHello() {
		return "jsonHello";
	}

	@RequestMapping(value = "/produceJson", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String produceJson() {
		return "produceJson";
	}

	@RequestMapping(value = "/consumes-produce", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	public String consumesProduce() {
		return "consumesProduce";
	}
}
