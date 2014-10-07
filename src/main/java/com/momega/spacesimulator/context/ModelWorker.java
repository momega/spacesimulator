package com.momega.spacesimulator.context;

import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.CameraService;
import com.momega.spacesimulator.service.MotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by martin on 10/5/14.
 */
public class ModelWorker {

    private static final Logger logger = LoggerFactory.getLogger(ModelWorker.class);

    @Autowired
    private MotionService motionService;

    @Autowired
    private CameraService cameraService;

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

}
