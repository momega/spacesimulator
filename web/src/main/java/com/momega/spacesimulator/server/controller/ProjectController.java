/**
 * 
 */
package com.momega.spacesimulator.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.server.data.ModelDatabase;
import com.momega.spacesimulator.server.data.ModelExecutor;
import com.momega.spacesimulator.server.data.ModelRunnable;

/**
 * @author martin
 *
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
	
	@Autowired
	private ModelDatabase modelDatabase;
	
	@Autowired
	private ModelExecutor modelExecutor;
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class); 
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Project> list() {
		List<Project> result = new ArrayList<>();
		for(Map.Entry<Integer, ModelRunnable> entry : modelDatabase.getAll().entrySet()) {
			ModelRunnable runnable = entry.getValue();
			Project p = createProject(entry.getKey().intValue(), runnable);
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
	public Project get(@PathVariable("id") int id) {
		logger.info("get id = {}", id);
		ModelRunnable runable = modelDatabase.get(id);
		Project p = createProject(id, runable);
		return p;
	}
	
	protected Project createProject(int id, ModelRunnable runnable) {
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
}
