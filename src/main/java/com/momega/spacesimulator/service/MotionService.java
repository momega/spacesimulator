package com.momega.spacesimulator.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.model.Trajectory;

/**
 * The motion service is responsible for computing new position of moving objects such as planets and satellites.
 * Generally it computes new time and then call two services {@link com.momega.spacesimulator.service.TrajectoryService}
 * and {@link com.momega.spacesimulator.service.RotationService} to compute new position and rotation of all registered
 * moving objects
 * Created by martin on 5/25/14.
 */
@Component
public class MotionService {

    private static final Logger logger = LoggerFactory.getLogger(MotionService.class);
    
    @Autowired
    private List<Propagator> propagators = new ArrayList<>();

    public Timestamp move(Timestamp time, double warpFactor) {
        Timestamp newTimestamp = time.add(warpFactor);
        logger.debug("time={}", newTimestamp.getValue());
        if (warpFactor > 0) {
            for (MovingObject mo : ModelHolder.getModel().getMovingObjects()) {
                move(mo, newTimestamp);
                mo.setTimestamp(newTimestamp);
            }
        }
        return newTimestamp;
    }
    
    /**
     * Computes the position of and object in the time newTimestamp. The set new position, velocity and orientation
     * @param movingObject the moving objects
     * @param newTimestamp new timestamp
     */
    public void move(MovingObject movingObject, Timestamp newTimestamp) {
        logger.debug("new time = {}", newTimestamp);
        Assert.notNull(movingObject);
        Trajectory trajectory = movingObject.getTrajectory();
        Assert.notNull(trajectory);
        for (Propagator propagator : propagators) {
            if (propagator.supports(movingObject)) {
                propagator.computePosition(movingObject, newTimestamp);
            }
        }
    }    

}
