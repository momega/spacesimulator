package com.momega.spacesimulator.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  
  private static final Logger logger = LoggerFactory.getLogger(ModelController.class); 
	
	@Autowired
	private ModelDatabase modelDatabase;
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public Model get(@PathVariable("id") int id) {
   	logger.info("get id = {}", id);
		Model m =  modelDatabase.getModel(id).getModel();
		return m;
	}
}
