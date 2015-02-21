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
import com.momega.spacesimulator.service.ModelRunnable;

/**
 * @author martin
 *
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
	
	@Autowired
	private ModelDatabase modelDatabase;
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class); 
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Project> list() {
		List<Project> result = new ArrayList<>();
		for(Map.Entry<Integer, ModelRunnable> entry : modelDatabase.getModels().entrySet()) {
			Project p = createProject(entry.getKey().intValue(), entry.getValue().getModel());
			result.add(p);
		}
		return result;
	}
	
	@RequestMapping(value = "/close/{id}", method = RequestMethod.GET)
	public int close(@PathVariable("id") int id) {
		logger.info("closing id = {}", id);
		ModelRunnable runnable = modelDatabase.remove(id);
		runnable.setRunning(false);
		return id;
	}

	@RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
	public Project getItem(@PathVariable("id") int id) {
		logger.info("id = {}", id);
		Model m = modelDatabase.getModel(id).getModel();
		Project p = createProject(id, m);
		return p;
	}
	
	protected Project createProject(int id, Model m) {
		Project p = new Project();
		p.setId(id);
		p.setName(m.getName());
		p.setTime(m.getTime());
		for(MovingObject mo : m.getMovingObjects()) {
			Texture texture = new Texture();
			if (mo instanceof CelestialBody) {
				CelestialBody celestialBody = (CelestialBody) mo;
				texture.setName(mo.getName());
				texture.setTextureFileName(celestialBody.getTextureFileName());
				p.getMovingObjects().add(texture);
			}
		}
		return p;
	}
}
