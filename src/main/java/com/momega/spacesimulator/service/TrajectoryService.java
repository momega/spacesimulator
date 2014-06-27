package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Trajectory Service calculates the position of all objects
 * Created by martin on 5/21/14.
 */
@Component
public class TrajectoryService {

    private static final Logger logger = LoggerFactory.getLogger(TrajectoryService.class);

    @Autowired
    private List<TrajectoryManager> trajectoryManagers = new ArrayList<>();

    /**
     * Computes the position of and object in the time newTimestamp. The set new position, velocity and orientation
     * @param movingObject the moving objects
     * @param newTime new timestamp
     */
    public void move(MovingObject movingObject, Timestamp newTime) {
        logger.debug("new time = {}", newTime);
        for(TrajectoryManager m : trajectoryManagers) {
            Assert.notNull(movingObject);
            Assert.notNull(movingObject.getTrajectory());
            if (m.supports(movingObject)) {
                m.computePosition(movingObject, newTime);
                movingObject.setTimestamp(newTime);
                return;
            }
        }
        throw new IllegalArgumentException("object " + movingObject.getName() + " has unknown trajectory type");
    }

    public void setTrajectoryManagers(List<TrajectoryManager> trajectoryManagers) {
        this.trajectoryManagers = trajectoryManagers;
    }
}
