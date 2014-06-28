package com.momega.spacesimulator.context;

import com.momega.spacesimulator.builder.AbstractModelBuilder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.CameraService;
import com.momega.spacesimulator.service.MotionService;
import com.momega.spacesimulator.utils.TimeUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by martin on 6/18/14.
 */
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private DateTimeFormatter formatter = ISODateTimeFormat.dateTime();

    private MotionService motionService;
    private CameraService cameraService;
    private final ApplicationContext applicationContext;

    public Application() {
        applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        this.motionService = applicationContext.getBean(MotionService.class);
        this.cameraService = applicationContext.getBean(CameraService.class);
    }

    public Model init(AbstractModelBuilder modelBuilder) {
        modelBuilder.init();
        logger.info("time = {}", formatter.print(TimeUtils.getDateTime(ModelHolder.getModel().getTime())));
        next();
        modelBuilder.initSatellites();
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
        logger.info("dispose time = {}",  formatter.print(TimeUtils.getDateTime(model.getTime())));
    }

}
