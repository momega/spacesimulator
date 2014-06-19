package com.momega.spacesimulator.context;

import com.momega.spacesimulator.builder.AbstractModelBuilder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.CameraService;
import com.momega.spacesimulator.service.MotionService;
import org.springframework.context.ApplicationContext;

/**
 * Created by martin on 6/18/14.
 */
public class Application {

    private final ApplicationContext applicationContext;
    private MotionService motionService;
    private CameraService cameraService;

    public Application(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.motionService = applicationContext.getBean(MotionService.class);
        this.cameraService = applicationContext.getBean(CameraService.class);
    }

    public Model init(AbstractModelBuilder modelBuilder) {
        modelBuilder.init();
        next();
        modelBuilder.initSatellites();
        predict();
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
    }

    public void predict() {
        motionService.predict();
    }

}
