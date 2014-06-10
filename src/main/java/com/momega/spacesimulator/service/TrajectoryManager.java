package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.model.Trajectory;

/**
 * Created by martin on 5/21/14.
 */
public interface TrajectoryManager {

    /**
     * Computes the position of and object in the time newTimestamp. The set new position, velocity and orientation
     * @param movingObject
     * @param newTimestamp new timestamp
     */
    void computePosition(MovingObject movingObject, Timestamp newTimestamp);

    /**
     * Computes the prediction of the trajectory
     * @param movingObject
     * @return
     */
    void computePrediction(MovingObject movingObject);

    /**
     * Indicates whether or not the service supports the trajectory
     * @param trajectory the trajectory
     * @return returns boolean value
     */
    boolean supports(Trajectory trajectory);
}
