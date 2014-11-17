package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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

    @Autowired
    private RotationService rotationService;

    @Autowired
    private HistoryPointService historyPointService;

    public Timestamp move(Timestamp time, BigDecimal warpFactor) {
        Timestamp newTimestamp = time.add(warpFactor);
        logger.debug("time={}", newTimestamp.getValue());
        if (!warpFactor.equals(BigDecimal.ZERO)) {
            for (MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
                if (dp instanceof RotatingObject && !ModelHolder.getModel().isRunningHeadless()) {
                    rotationService.rotate((RotatingObject) dp, newTimestamp);
                }
                trajectoryService.move(dp, newTimestamp);
                if (dp instanceof Spacecraft) {
                    Spacecraft spacecraft = (Spacecraft) dp;
                    historyPointService.updateHistory(spacecraft);
                }
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

    public void setHistoryPointService(HistoryPointService historyPointService) {
        this.historyPointService = historyPointService;
    }
}
