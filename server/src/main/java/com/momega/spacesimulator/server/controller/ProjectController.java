/**
 * 
 */
package com.momega.spacesimulator.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.server.data.ModelDatabase;
import com.momega.spacesimulator.service.ModelBuilderFactory;
import com.momega.spacesimulator.service.ModelRunnable;
import com.momega.spacesimulator.service.ModelWorker;

/**
 * @author martin
 *
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class); 
	
	private static final String BUILDERS_PACKAGE = "com.momega.spacesimulator.builder";
	
	@Autowired
	private ModelDatabase modelDatabase;
	
	@Autowired
	private ModelBuilderFactory modelBuilderFactory;
	
	@Autowired
	private ModelWorker modelWorker;
	
	@Autowired
	private TaskExecutor taskExecutor;

	@RequestMapping(value = "/build.do/{builderName}", method = RequestMethod.GET)
	public int build(@PathVariable("builderName") String builderName) {
		logger.info("builder name = {}", builderName);
		ModelBuilder modelBuilder = modelBuilderFactory.createBuilder(BUILDERS_PACKAGE + "." + builderName);
		Model model = modelBuilderFactory.init(modelBuilder);
		int id = modelDatabase.add(model);
		
		ModelRunnable runnable = new ModelRunnable(modelWorker, model, 1.0, true);
		taskExecutor.execute(runnable);
		
		logger.info("model with id executed {}", id);
		
		return id;
	}
}
