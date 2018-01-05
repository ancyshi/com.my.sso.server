package com.my.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemplateController {
	@RequestMapping("/helloHtml")
	public String helloHtml(Map<String, Object> map) {

		map.put("hello", "from TemplateController.helloHtml");
		return "/helloHtml";
	}
}
