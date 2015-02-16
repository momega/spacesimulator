package com.momega.spacesimulator.conroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.momega.spacesimulator.model.Model;

@RestController
@RequestMapping("/model")
public class ModelController {

	@RequestMapping(value = "/get.do", method = RequestMethod.GET)
	public Model getModel() {
		Model m =  new Model();
		m.setName("Baf");
		return m;
	}
}
