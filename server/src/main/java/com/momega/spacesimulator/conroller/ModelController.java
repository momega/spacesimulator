package com.momega.spacesimulator.conroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.momega.spacesimulator.builder.VenusSpacecraftModelBuilder;
import com.momega.spacesimulator.model.Model;

@RestController
@RequestMapping("/model")
public class ModelController {
	
	@Autowired
	private VenusSpacecraftModelBuilder modelBuilder;
	
	@RequestMapping(value = "/get.do", method = RequestMethod.GET)
	public Model getModel() {
		Model m =  modelBuilder.build();
		return m;
	}
}
