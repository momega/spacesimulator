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

    private static Application instance = null;

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    private final ApplicationContext applicationContext;
    private final ModelWorker modelWorker;
	private final ModelBuilder modelBuilder;

    private Application() {
        applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        modelWorker = applicationContext.getBean(ModelWorker.class);
        modelBuilder = applicationContext.getBean(ModelBuilder.class);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public <T> T getService(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }
    
    public void next(boolean runningHeadless) {
    	modelWorker.next(runningHeadless);
    }

    public void init(long seconds) {
        Model model = modelBuilder.build();
        ModelHolder.replaceModel(model);

        logger.info("time = {}", TimeUtils.timeAsString(model.getTime()));
        Timestamp showTime = model.getTime().add(BigDecimal.valueOf(seconds));
        logger.info("show time = {}", TimeUtils.timeAsString(showTime));
        while(model.getTime().compareTo(showTime)<=0) {
        	modelWorker.next(true);
        }
        
        logger.info("model data built");
    }

    public void dispose() {
        logger.info("dispose time = {}",  TimeUtils.timeAsString(ModelHolder.getModel().getTime()));
    }

}
