package com.momega.spacesimulator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Timestamp;

/**
 * The motion service is responsible for computing new position of dynamical points such as planets and satellites.
 * Generally it computes new time and then call two services {@link com.momega.spacesimulator.service.TrajectoryService}
 * and {@link com.momega.spacesimulator.service.RotationService} to compute new position and rotation of all registered
 * dynamical points
 * Created by martin on 5/25/14.
 */
@Component
public class MotionService {

    private static final Logger logger = LoggerFactory.getLogger(MotionService.class);

    @Autowired
    private TrajectoryService trajectoryService;

    public Timestamp move(Timestamp time, double warpFactor) {
        Timestamp newTimestamp = time.add(warpFactor);
        logger.debug("time={}", newTimestamp.getValue());
        if (warpFactor > 0) {
            for (MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
                trajectoryService.move(dp, newTimestamp);
            }
        }
        return newTimestamp;
    }

}
