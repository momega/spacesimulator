package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.model.RotatingObject;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * The motion service is responsible for computing new position of dynamical points such as planets and satellites.
 * Generally it computes new time and then call two services {@link com.momega.spacesimulator.service.TrajectoryService}
 * and {@link com.momega.spacesimulator.service.RotationService} to compute new position and rotation of all registered
 * dynamical points
 * Created by martin on 5/25/14.
 */
public class MotionService {

    private static final Logger logger = LoggerFactory.getLogger(MotionService.class);

    private TrajectoryService trajectoryService;

    private RotationService rotationService;

    private UniverseService universeService;

    public Timestamp move(Timestamp time, BigDecimal warpFactor) {
        BigDecimal timestamp = time.getValue().add(warpFactor);
        Timestamp newTimestamp = TimeUtils.newTime(timestamp);
        logger.debug("time={}", timestamp);
        for(DynamicalPoint dp : universeService.getDynamicalPoints()) {
            trajectoryService.move(dp, newTimestamp);
            if (dp instanceof RotatingObject) {
                rotationService.rotate((RotatingObject) dp, newTimestamp);
            }
        }
        return newTimestamp;
    }

    public void setTrajectoryService(TrajectoryService trajectoryService) {
        this.trajectoryService = trajectoryService;
    }

    public void setRotationService(RotationService rotationService) {
        this.rotationService = rotationService;
    }

    public void setUniverseService(UniverseService universeService) {
        this.universeService = universeService;
    }

    public void predict() {
        for(DynamicalPoint dp : universeService.getDynamicalPoints()) {
            trajectoryService.prediction(dp);
        }
    }
}
