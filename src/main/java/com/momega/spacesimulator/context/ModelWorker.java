package com.momega.spacesimulator.context;

import java.math.BigDecimal;

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
     * @param runningHeadless running headless
     * @param warpFactor the warp factor
     */
    public void next(boolean runningHeadless, BigDecimal warpFactor) {
        Model model = ModelHolder.getModel();
        model.setRunningHeadless(runningHeadless);
        Timestamp t = motionService.move(model.getTime(), warpFactor);
        model.setTime(t);
        if (!model.isRunningHeadless()) {
	        cameraService.updatePosition(model.getCamera());
	        logger.debug("time = {}", model.getTime());
        }
    }

}
