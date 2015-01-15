package com.momega.spacesimulator.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.momega.common.AbstractSpringApplication;
import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.RunStep;
import com.momega.spacesimulator.service.ModelWorker;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * The default implementation of the root application object. The different subclasses may be used for
 * real application and for tests
 * Created by martin on 12/29/14.
 */
public class DefaultApplication extends AbstractSpringApplication {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private final ModelWorker modelWorker;

    public DefaultApplication(Class<?> configClass) {
        super(configClass);
        modelWorker = getService(ModelWorker.class);
    }

    public void next(RunStep step) {
        modelWorker.next(step);
    }

    public ModelBuilder createBuilder(String builderClassName) {
        try {
            Class<?> clazz = Class.forName(builderClassName);
            Assert.isAssignable(ModelBuilder.class, clazz);
            return (ModelBuilder) getBeanOfClass(clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid class name to load " + builderClassName, e);
        }
    }

    public void init(Class<? extends ModelBuilder> modelBuilderClass) {
        ModelBuilder modelBuilder = getBeanOfClass(modelBuilderClass);
        init(modelBuilder);
    }

    public void init(ModelBuilder modelBuilder) {
        Model model = modelBuilder.build();
        ModelHolder.setModel(model);
        logger.info("execute first second at time = {}", TimeUtils.timeAsString(model.getTime()));

        RunStep step = RunStep.create(model.getTime(), 1.0, true);
        next(step);
        logger.info("model data built");
    }

    public void dispose() {
        logger.info("dispose time = {}",  TimeUtils.timeAsString(ModelHolder.getModel().getTime()));
    }
}
