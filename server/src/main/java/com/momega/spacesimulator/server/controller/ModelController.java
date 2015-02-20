package com.momega.spacesimulator.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.server.data.ModelDatabase;

@RestController
@RequestMapping("/model")
public class ModelController {
	
	@Autowired
	private ModelDatabase modelDatabase;
	
	@RequestMapping(value = "/get.do/{id}", method = RequestMethod.GET)
	public Model getModel(@PathVariable("id") int id) {
		Model m =  modelDatabase.getModel(id);
		return m;
	}
}
