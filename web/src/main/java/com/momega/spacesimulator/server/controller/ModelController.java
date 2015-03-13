package com.momega.spacesimulator.server.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.server.data.BuilderDatabase;
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
	private BuilderDatabase builderDatabase;
	
	@Autowired
	private ModelExecutor modelExecutor;
	
	@Autowired
	private ModelSerializer modelSerializer;		
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public Model get(@PathVariable("id") int id) {
		logger.info("get id = {}", id);
		ModelRunnable runnable = modelDatabase.get(id);

		return getModelSnapshot(id, runnable);
	}
	
	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> download(@PathVariable("id") int id) throws IOException {
		logger.info("get id = {}", id);
		ModelRunnable runnable = modelDatabase.get(id);
		
		Model m = getModelSnapshot(id, runnable);
		byte[] bytes = modelSerializer.toBytes(m);
		ByteArrayResource resource = new ByteArrayResource(bytes);
		
		HttpHeaders respHeaders = new HttpHeaders();
	    respHeaders.setContentType(new MediaType("application", "json"));
	    respHeaders.setContentLength(resource.contentLength());
	    respHeaders.setContentDispositionFormData("attachment", m.getName() + ".json");
		
		ResponseEntity<ByteArrayResource> result = new ResponseEntity<ByteArrayResource>(resource, respHeaders, HttpStatus.OK);
		return result;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Project> list() {
		List<Project> result = new ArrayList<>();
		for(Map.Entry<Integer, ModelRunnable> entry : modelDatabase.getAll().entrySet()) {
			ModelRunnable runnable = entry.getValue();
			Project p = toProject(entry.getKey().intValue(), runnable);
			result.add(p);
		}
		return result;
	}
	
	@RequestMapping(value = "/stop/{id}", method = RequestMethod.GET)
	public int stop(@PathVariable("id") int id) {
		ModelRunnable runnable = modelDatabase.get(id);
		if (runnable != null) {
			modelExecutor.stop(id, runnable);
		}
		return id;
	}
	
	@RequestMapping(value = "/resume/{id}", method = RequestMethod.GET)
	public int resume(@PathVariable("id") int id) {
		Model model = modelDatabase.get(id).getModel();
		ModelRunnable runnable = modelExecutor.create(model);
		modelDatabase.add(id, runnable);
		modelExecutor.start(id, runnable);
		return id;
	}
	
	@RequestMapping(value = "/close/{id}", method = RequestMethod.GET)
	public int close(@PathVariable("id") int id) {
		logger.info("closing id = {}", id);
		stop(id);
		modelDatabase.remove(id);
		return id;
	}

	@RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
	public Project getProject(@PathVariable("id") int id) {
		logger.info("get project, id = {}", id);
		ModelRunnable runable = modelDatabase.get(id);
		Project p = toProject(id, runable);
		return p;
	}
	
	@RequestMapping(value = "/time/{id}", method = RequestMethod.GET)
	public Timestamp getTime(@PathVariable("id") int id) {
		logger.info("get time, id = {}", id);
		ModelRunnable runnable = modelDatabase.get(id);
		return runnable.getModel().getTime();
	}	
	
	@RequestMapping(value = "/snapshot/{id}", method = RequestMethod.GET)
	public int takeSnapshot(@PathVariable("id") int id) {
		logger.info("create builder, id = {}", id);
		ModelRunnable runnable = modelDatabase.get(id);
		Model m = getModelSnapshot(id, runnable);
		
		ModelRunnable newRunnable = modelExecutor.create(m);
		newRunnable.setRunning(false);
		int modelId = modelDatabase.add(newRunnable);
		logger.info("model with id created {}", modelId);
		return modelId;
	}
	
	protected Project toProject(int id, ModelRunnable runnable) {
		Project p = new Project();
		Model m = runnable.getModel();
		p.setId(id);
		p.setName(m.getName());
		p.setTime(m.getTime());
		p.setRunning(runnable.isRunning());
		p.setLastHistoryPoint(runnable.getLastHistoryPoint());
		for(MovingObject mo : m.getMovingObjects()) {
			Texture texture = new Texture();
			if (mo instanceof CelestialBody) {
				CelestialBody celestialBody = (CelestialBody) mo;
				texture.setName(mo.getName());
				texture.setTextureFileName(celestialBody.getTextureFileName());
				p.getCelestialBodies().add(texture);
			}
		}
		return p;
	}	
	
	protected Model getModelSnapshot(int id, ModelRunnable runnable) {
		if (runnable.isRunning()) {
			Model m = modelExecutor.stop(id, runnable);
			List<HistoryPoint> historyPoints = runnable.getHistoryPoints(); // copy history points
			Model result = modelSerializer.clone(m);
			runnable = modelExecutor.create(m);
			runnable.addHistoryPoints(historyPoints);
			modelDatabase.add(id, runnable);
			modelExecutor.start(id, runnable);
			return result;
		} else {
			Model m = runnable.getModel();
			Model result = modelSerializer.clone(m);
			return result;
		}
	}	
	
}
