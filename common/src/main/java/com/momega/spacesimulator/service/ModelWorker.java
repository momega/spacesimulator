package com.momega.spacesimulator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.RunStep;
import com.momega.spacesimulator.model.Timestamp;

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
     * Next step of the model iteration. If the model is unspecified, the method does nothing
     * @param model the model
     * @param step the run step
     */
    public void next(Model model, RunStep step) {
        if (model == null) {
            return;
        }
        motionService.move(model, step);
        Timestamp t = step.getNewTimestamp();
        logger.debug("time = {}", t);
        model.setTime(t);
        if (!step.isRunningHeadless()) {
	        cameraService.updatePosition(model.getCamera());
        }
    }

}
