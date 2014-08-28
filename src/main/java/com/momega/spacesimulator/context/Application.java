package com.momega.spacesimulator.context;

import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.CameraService;
import com.momega.spacesimulator.service.MotionService;
import com.momega.spacesimulator.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by martin on 6/18/14.
 */
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private MotionService motionService;
    private CameraService cameraService;
    private final ApplicationContext applicationContext;

    public Application() {
        applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        this.motionService = applicationContext.getBean(MotionService.class);
        this.cameraService = applicationContext.getBean(CameraService.class);
    }

    public Model init(ModelBuilder modelBuilder) {
        modelBuilder.build();
        logger.info("time = {}", TimeUtils.timeAsString(ModelHolder.getModel().getTime()));
        next();
        logger.info("model data built");
        return ModelHolder.getModel();
    }

    /**
     * Next step of the model iteration
     */
    public void next() {
        Model model = ModelHolder.getModel();
        Timestamp t = motionService.move(model.getTime(), model.getWarpFactor());
        model.setTime(t);
        cameraService.updatePosition(model.getCamera());
        logger.debug("time = {}", model.getTime());
    }

    public void dispose() {
        Model model = ModelHolder.getModel();
        logger.info("dispose time = {}",  TimeUtils.timeAsString(ModelHolder.getModel().getTime()));
    }

}
