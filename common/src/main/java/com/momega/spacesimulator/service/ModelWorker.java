package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 10/5/14.
 */
@Component
public class ModelWorker {

    private static final Logger logger = LoggerFactory.getLogger(ModelWorker.class);

    @Autowired
    private MotionService motionService;

    @Autowired
    private CameraService cameraService;

    /**
     * Next step of the model iteration
     * @param runningHeadless running headless
     * @param warpFactor the warp factor
     */
    public void next(boolean runningHeadless, double warpFactor) {
        Model model = ModelHolder.getModel();
        if (model == null) {
            return;
        }
        model.setRunningHeadless(runningHeadless);
        Timestamp t = motionService.move(model.getTime(), warpFactor);
        model.setTime(t);
        if (!model.isRunningHeadless()) {
	        cameraService.updatePosition(model.getCamera());
	        logger.debug("time = {}", model.getTime());
        }
    }

}
