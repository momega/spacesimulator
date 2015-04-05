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
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.server.data.Collection;
import com.momega.spacesimulator.server.data.ModelExecutor;
import com.momega.spacesimulator.server.data.ModelRunnable;
import com.momega.spacesimulator.service.ModelSerializer;

@RestController
@RequestMapping("/model")
public class ModelController {
  
  private static final Logger logger = LoggerFactory.getLogger(ModelController.class); 
	
	@Autowired
	private Collection<Model> modelCollection;
	
	@Autowired
	private Collection<Builder> builderCollection;
	
	@Autowired
	private ModelExecutor modelExecutor;
	
	@Autowired
	private ModelSerializer modelSerializer;		
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public Model get(@PathVariable("id") String id) {
		logger.info("get id = {}", id);
		ModelRunnable runnable = modelExecutor.get(id);

		return getModelSnapshot(id, runnable);
	}
	
	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> download(@PathVariable("id") String id) throws IOException {
		logger.info("get id = {}", id);
		ModelRunnable runnable = modelExecutor.get(id);
		
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
		for(Map.Entry<String, Model> entry : modelCollection.getAll().entrySet()) {
			String id = entry.getKey();
			Model model = entry.getValue();
			Project p = toProject(id, model);
			result.add(p);
		}
		return result;
	}
	
	@RequestMapping(value = "/stop/{id}", method = RequestMethod.GET)
	public String stop(@PathVariable("id") String id) {
		modelExecutor.stop(id);
		return id;
	}
	
	@RequestMapping(value = "/resume/{id}", method = RequestMethod.GET)
	public String resume(@PathVariable("id") String id) {
		Model model = modelCollection.get(id);
		modelCollection.update(id, model);
		modelExecutor.create(model, id);
		return id;
	}
	
	@RequestMapping(value = "/close/{id}", method = RequestMethod.GET)
	public String close(@PathVariable("id") String id) {
		logger.info("closing id = {}", id);
		stop(id);
		modelCollection.remove(id);
		return id;
	}

	@RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
	public Project getProject(@PathVariable("id") String id) {
		logger.info("get project, id = {}", id);
		Model model = modelCollection.get(id);
		Project p = toProject(id, model);
		return p;
	}
	
	@RequestMapping(value = "/time/{id}", method = RequestMethod.GET)
	public Timestamp getTime(@PathVariable("id") String id) {
		logger.info("get time, id = {}", id);
		ModelRunnable runnable = modelExecutor.get(id);
		return runnable.getModel().getTime();
	}	
	
	@RequestMapping(value = "/snapshot/{id}", method = RequestMethod.GET)
	public String takeSnapshot(@PathVariable("id") String id) {
		logger.info("create builder, id = {}", id);
		ModelRunnable runnable = modelExecutor.get(id);
		Model m = getModelSnapshot(id, runnable);
		String modelId = modelCollection.add(m);
		logger.info("model with id created {}", modelId);
		return modelId;
	}
	
	protected Project toProject(String id, Model m) {
		ModelRunnable runnable = modelExecutor.get(id);
		boolean isRunning = false;
		if (runnable != null) {
			isRunning = runnable.isRunning();
		}
		
		Project p = new Project();
		p.setId(id);
		p.setName(m.getName());
		p.setTime(m.getTime());
		p.setRunning(isRunning);
		HistoryPoint lastHistoryPoint = null;
		for(MovingObject mo : m.getMovingObjects()) {
			Texture texture = new Texture();
			if (mo instanceof CelestialBody) {
				CelestialBody celestialBody = (CelestialBody) mo;
				texture.setName(mo.getName());
				texture.setTextureFileName(celestialBody.getTextureFileName());
				p.getCelestialBodies().add(texture);
			}
			else if (mo instanceof Spacecraft) {
				Spacecraft spacecraft = (Spacecraft) mo;
				if (!spacecraft.getNamedHistoryPoints().isEmpty()) {
					lastHistoryPoint = compareHistoryPoint(lastHistoryPoint, spacecraft.getNamedHistoryPoints().get(spacecraft.getNamedHistoryPoints().size()-1));
				}
			}
		}
		p.setLastHistoryPoint(lastHistoryPoint);
		return p;
	}	
	
	protected HistoryPoint compareHistoryPoint(HistoryPoint hp1, HistoryPoint hp2) {
		if (hp1 == null) {
			return hp2;
		} else if (hp2 == null) {
			return hp1;
		} else {
			if (hp1.getTimestamp().compareTo(hp2.getTimestamp())>=0) {
				return hp1;
			} else {
				return hp2;
			}
		}
	}
	
	protected Model getModelSnapshot(String id, ModelRunnable runnable) {
		if (runnable.isRunning()) {
			Model m = modelExecutor.stop(id);
			Model result = modelSerializer.clone(m);
			modelCollection.update(id, m);
			modelExecutor.create(m, id);
			return result;
		} else {
			Model m = runnable.getModel();
			Model result = modelSerializer.clone(m);
			return result;
		}
	}	
	
}
