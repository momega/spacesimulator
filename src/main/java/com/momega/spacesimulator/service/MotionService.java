package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.model.RotatingObject;
import com.momega.spacesimulator.model.Time;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

    public void move(List<DynamicalPoint> dynamicalPoints, Time time) {
        double timestamp = time.getTimestamp() + time.getWarpFactor();
        time.setTimestamp(timestamp);
        logger.info("time={}", timestamp);
        for(DynamicalPoint dp : dynamicalPoints) {
            trajectoryService.move(dp, timestamp);
            if (dp instanceof RotatingObject) {
                rotationService.rotate((RotatingObject) dp, timestamp);
            }
        }
    }

    public void setTrajectoryService(TrajectoryService trajectoryService) {
        this.trajectoryService = trajectoryService;
    }

    public void setRotationService(RotationService rotationService) {
        this.rotationService = rotationService;
    }
}
