package com.momega.spacesimulator.context;

import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.ModelWorker;
import com.momega.spacesimulator.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;

/**
 * The default implementation of the root application object. The different subclasses may be used for
 * real application and for tests
 * Created by martin on 12/29/14.
 */
public class DefaultApplication {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private final ApplicationContext applicationContext;
    private final ModelWorker modelWorker;

    public DefaultApplication(Class<?> configClass) {
        applicationContext = new AnnotationConfigApplicationContext(configClass);
        modelWorker = applicationContext.getBean(ModelWorker.class);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public <T> T getService(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public void next(boolean runningHeadless, BigDecimal warpFactor) {
        modelWorker.next(runningHeadless, warpFactor);
    }

    public void init(long seconds) {
        ModelBuilder modelBuilder = applicationContext.getBean(ModelBuilder.class);
        Model model = modelBuilder.build();
        ModelHolder.replaceModel(model);

        logger.info("time = {}", TimeUtils.timeAsString(model.getTime()));
        Timestamp showTime = model.getTime().add(BigDecimal.valueOf(seconds));
        logger.info("show time = {}", TimeUtils.timeAsString(showTime));
        while(model.getTime().compareTo(showTime)<=0) {
            modelWorker.next(true, BigDecimal.ONE);
        }

        logger.info("model data built");
    }

    public void dispose() {
        logger.info("dispose time = {}",  TimeUtils.timeAsString(ModelHolder.getModel().getTime()));
    }
}
