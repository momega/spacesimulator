package com.momega.spacesimulator.server.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

		return getAndRestart(id, runnable);
	}
	
	protected Model getAndRestart(int id, ModelRunnable runnable) {
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
	
	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> download(@PathVariable("id") int id) throws IOException {
		logger.info("get id = {}", id);
		ModelRunnable runnable = modelDatabase.get(id);
		
		Model m = getAndRestart(id, runnable);
		StringWriter writer = new StringWriter();
		modelSerializer.save(m, writer);
		writer.flush();
		writer.close();
		
		ByteArrayResource resource = new ByteArrayResource(writer.getBuffer().toString().getBytes());
		
		HttpHeaders respHeaders = new HttpHeaders();
	    respHeaders.setContentType(new MediaType("application", "json"));
	    respHeaders.setContentLength(resource.contentLength());
	    respHeaders.setContentDispositionFormData("attachment", m.getName() + ".json");
		
		ResponseEntity<ByteArrayResource> result = new ResponseEntity<ByteArrayResource>(resource, respHeaders, HttpStatus.OK);
		return result;
	}
	
	@RequestMapping(value = "/time/{id}", method = RequestMethod.GET)
	public Timestamp time(@PathVariable("id") int id) {
		logger.info("get time for id = {}", id);
		ModelRunnable runnable = modelDatabase.get(id);
		return runnable.getModel().getTime();
	}
	
}
