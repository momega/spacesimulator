package com.momega.spacesimulator.server.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.server.data.ModelDatabase;
import com.momega.spacesimulator.server.data.ModelExecutor;
import com.momega.spacesimulator.server.data.ModelRunnable;
import com.momega.spacesimulator.service.ModelSerializer;

@RestController
@RequestMapping("/model")
public class ModelController {
  
  private static final Logger logger = LoggerFactory.getLogger(ModelController.class); 
	
	@Autowired
	private ModelDatabase modelDatabase;
	
	@Autowired
	private ModelExecutor modelExecutor;
	
	@Autowired
	private ModelSerializer modelSerializer;		
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public Model get(@PathVariable("id") int id) {
		logger.info("get id = {}", id);
		ModelRunnable runnable = modelDatabase.get(id);
		
		// TODO: re-factor this part of the code
		Model m = modelExecutor.stop(id, runnable);
		List<HistoryPoint> historyPoints = runnable.getHistoryPoints(); // copy history points
		Model result = modelSerializer.clone(m);
		runnable = modelExecutor.create(m);
		runnable.addHistoryPoints(historyPoints);
		modelDatabase.add(id, runnable);
		modelExecutor.start(id, runnable);
		return result;
	}
	
	@RequestMapping(value = "/time/{id}", method = RequestMethod.GET)
	public Timestamp time(@PathVariable("id") int id) {
		logger.info("get time for id = {}", id);
		ModelRunnable runnable = modelDatabase.get(id);
		return runnable.getModel().getTime();
	}
	
}
