package com.momega.spacesimulator.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.RunStep;
import com.momega.spacesimulator.utils.TimeUtils;

@Component
public class ModelBuilderFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(ModelBuilderFactory.class);
	
	@Autowired
	private ModelWorker modelWorker;
	
	@Autowired
	private ApplicationContext applicationContext;

	public ModelBuilder createBuilder(String builderClassName) {
        try {
            Class<?> clazz = Class.forName(builderClassName);
            Assert.isAssignable(ModelBuilder.class, clazz);
            return (ModelBuilder) getBeanOfClass(clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid class name to load " + builderClassName, e);
        }
    }

    public Model init(Class<? extends ModelBuilder> modelBuilderClass) {
        ModelBuilder modelBuilder = getBeanOfClass(modelBuilderClass);
        Model model = init(modelBuilder);
        return model;
    }

    public Model init(ModelBuilder modelBuilder) {
        Model model = modelBuilder.build();
        logger.info("execute first second at time = {}", TimeUtils.timeAsString(model.getTime()));

        RunStep step = RunStep.create(model.getTime(), 1.0, false);
        modelWorker.next(model, step);
        logger.info("model data built");
        return model;
    }
    
    private <T> T getBeanOfClass(Class<T> clazz) {
        Map<String, T> beans = applicationContext.getBeansOfType(clazz);
        if (beans.size()==0) {
            throw new IllegalArgumentException("no such bean of specified class registered in the context");
        }
        for(Map.Entry<String, T> entry : beans.entrySet()) {
            T bean = entry.getValue();
            if (bean.getClass().equals(clazz)) {
                return bean;
            }
        }
        throw new IllegalArgumentException("no exact match bean found in the context");
    }    
}
