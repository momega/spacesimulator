package com.momega.spacesimulator.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

@RestController
@RequestMapping("/builder")
public class BuilderController {
	
	private static final Logger logger = LoggerFactory.getLogger(BuilderController.class); 
	
	private static final String BUILDERS_PACKAGE = "com.momega.spacesimulator.builder";

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private ModelDatabase modelDatabase;
	
	@Autowired
	private ModelBuilderFactory modelBuilderFactory;
	
	@Autowired
	private ModelWorker modelWorker;
	
	@Autowired
	private TaskExecutor taskExecutor;

	@RequestMapping(value = "/build/{builderName}", method = RequestMethod.GET)
	public int build(@PathVariable("builderName") String builderName) {
		logger.info("builder name = {}", builderName);
		ModelBuilder modelBuilder = modelBuilderFactory.createBuilder(BUILDERS_PACKAGE + "." + builderName);
		Model model = modelBuilderFactory.init(modelBuilder);
		
		ModelRunnable runnable = new ModelRunnable(modelWorker, model, 1.0, true);
		int id = modelDatabase.add(runnable);
		//taskExecutor.execute(runnable);
		
		logger.info("model with id executed {}", id);
		
		return id;
	}	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Builder> list() {
		Map<String, ModelBuilder> builders = applicationContext.getParent().getBeansOfType(ModelBuilder.class);
		List<Builder> result = new ArrayList<>();
		for(ModelBuilder mb : builders.values()) {
			Builder b = new Builder();
			b.setName(mb.getName());
			String className = mb.getClass().getSimpleName();
			b.setBuilderName(className);
			result.add(b);
		}
		return result;
	}
}
