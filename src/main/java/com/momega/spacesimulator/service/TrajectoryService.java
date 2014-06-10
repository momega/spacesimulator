package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 5/21/14.
 */
public class TrajectoryService {

    private static final Logger logger = LoggerFactory.getLogger(TrajectoryService.class);

    private List<TrajectoryManager> trajectoryManagers = new ArrayList<>();

    /**
     * Computes the position of and object in the time newTimestamp. The set new position, velocity and orientation
     * @param movingObject the moving objects
     * @param newTime new timestamp
     */
    public void move(MovingObject movingObject, Timestamp newTime) {
        for(TrajectoryManager m : trajectoryManagers) {
            if (m.supports(movingObject.getTrajectory())) {
                m.computePosition(movingObject, newTime);
                movingObject.setTimestamp(newTime);
                return;
            }
        }
        throw new IllegalArgumentException("object " + movingObject.getName() + " has unknown trajectory");
    }


    public void setTrajectoryManagers(List<TrajectoryManager> trajectoryManagers) {
        this.trajectoryManagers = trajectoryManagers;
    }
}
