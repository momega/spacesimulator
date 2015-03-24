package com.momega.spacesimulator.server.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.momega.spacesimulator.builder.AbstractModelBuilder;
import com.momega.spacesimulator.builder.ByteArrayModelBuilder;
import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.server.data.BuilderDatabase;
import com.momega.spacesimulator.server.data.ModelDatabase;
import com.momega.spacesimulator.server.data.ModelExecutor;
import com.momega.spacesimulator.service.ModelBuilderFactory;

@RestController
@RequestMapping("/builder")
public class BuilderController implements InitializingBean {
	
	private static final Logger logger = LoggerFactory.getLogger(BuilderController.class); 
	
	private static final String BUILDERS_PACKAGE = "com.momega.spacesimulator.builder";

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private ModelDatabase modelDatabase;
	
	@Autowired
	private BuilderDatabase builderDatabase;
	
	@Autowired
	private ModelExecutor modelExecutor;
	
	@Autowired
	private ModelBuilderFactory modelBuilderFactory;

	@RequestMapping(value = "/build/{id}", method = RequestMethod.GET)
	public int build(@PathVariable(value="id") int id) {
		logger.info("builder id = {}", id);
		Builder builder = builderDatabase.get(id);
		ModelBuilder modelBuilder = modelBuilderFactory.createBuilder(BUILDERS_PACKAGE + "." + builder.getBuilderClassName());
		if (modelBuilder instanceof ByteArrayModelBuilder) {
			ByteArrayModelBuilder byteArrayModelBuilder = (ByteArrayModelBuilder) modelBuilder;
			byteArrayModelBuilder.setData(builder.getData());
		}
		Model model = modelBuilderFactory.init(modelBuilder);
		
		int modelId = modelDatabase.add(model);
		modelExecutor.create(model, modelId);
		
		logger.info("model with id executed {}", modelId);
		
		return modelId;
	}
	
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public int remove(@PathVariable("id") int id) {
		builderDatabase.remove(id);
		return id;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Builder> list() {
		List<Builder> result = new ArrayList<>();
		for(Builder b : builderDatabase.getAll().values()) {
			result.add(b);
		}
		return result;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, ModelBuilder> builders = applicationContext.getParent().getBeansOfType(ModelBuilder.class);
		for(ModelBuilder mb : builders.values()) {
			if (mb instanceof AbstractModelBuilder) {
				Builder b = new Builder();
				b.setName(mb.getName());
				String className = mb.getClass().getSimpleName();
				b.setBuilderClassName(className);
				int id = builderDatabase.add(b);
				b.setId(id);
			}
		}
	}
	
	@RequestMapping(value = "/upload")
	@ResponseBody
	public Builder upload(@RequestParam("file") MultipartFile file) {
		try {
			String fileName = file.getOriginalFilename();
			byte[] bytes = file.getBytes();
			Builder builder = new Builder();
			builder.setFileName(fileName);
			builder.setData(bytes);
			builder.setBuilderClassName(ByteArrayModelBuilder.class.getSimpleName());
			builder.setName("Builder from file " + fileName);
			int id = builderDatabase.add(builder);
			builder.setId(id);
			logger.info("Server File Location=" + fileName);
			return builder;
		} catch (IOException e) {
			 throw new IllegalArgumentException("unable to uplaod model", e);
		}
	}	
}
