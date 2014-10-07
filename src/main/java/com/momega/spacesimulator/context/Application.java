package com.momega.spacesimulator.context;

import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;

/**
 * Created by martin on 6/18/14.
 */
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private final ApplicationContext applicationContext;

    public Application() {
        applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    public Model init(long seconds) {
        ModelBuilder modelBuilder = applicationContext.getBean(ModelBuilder.class);
        modelBuilder.build();

        ModelWorker modelWorker = applicationContext.getBean(ModelWorker.class);
        ModelHolder.setModelWorker(modelWorker);

        logger.info("time = {}", TimeUtils.timeAsString(ModelHolder.getModel().getTime()));
        Timestamp showTime = ModelHolder.getModel().getTime().add(BigDecimal.valueOf(seconds));
        logger.info("show time = {}", TimeUtils.timeAsString(showTime));
        while(ModelHolder.getModel().getTime().compareTo(showTime)<=0) {
        	modelWorker.next();
        }
        
        logger.info("model data built");
        return ModelHolder.getModel();
    }

    public void dispose() {
        logger.info("dispose time = {}",  TimeUtils.timeAsString(ModelHolder.getModel().getTime()));
    }

}
