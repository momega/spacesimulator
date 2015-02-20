package com.momega.spacesimulator.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.momega.spacesimulator.builder.ModelBuilder;

@RestController
@RequestMapping("/builder")
public class BuilderController {

	@Autowired
	private ApplicationContext applicationContext;
	
	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	public List<Builder> list() {
		Map<String, ModelBuilder> builders = applicationContext.getParent().getBeansOfType(ModelBuilder.class);
		List<Builder> result = new ArrayList<>();
		for(ModelBuilder mb : builders.values()) {
			Builder b = new Builder();
			b.setName(mb.getName());
			b.setBuilderName(mb.getClass().getName());
			result.add(b);
		}
		return result;
	}
}
