package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.RotatingObject;
import com.momega.spacesimulator.model.Time;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by martin on 5/25/14.
 */
public class MotionService {

    private static final Logger logger = LoggerFactory.getLogger(MotionService.class);

    private TrajectoryService trajectoryService;

    private RotationService rotationService;

    public void move(List<DynamicalPoint> dynamicalPoints, Time time) {
        DateTime timestamp = time.getTimestamp();
        long warp = (long) time.getWarpFactor();
        timestamp = timestamp.plus(warp * DateTimeConstants.MILLIS_PER_SECOND);
        time.setTimestamp(timestamp);
        logger.debug("time={}", timestamp);
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
