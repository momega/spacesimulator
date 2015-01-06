package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Timestamp;

/**
 * Created by martin on 5/21/14.
 */
public interface Propagator {

    /**
     * Computes the position of and object in the time newTimestamp. The set new position, velocity and orientation.
     * Some propagator may also set new timestamp
     * @param movingObject the moving object
     * @param newTimestamp new timestamp
     */
    void computePosition(MovingObject movingObject, Timestamp newTimestamp);

    /**
     * Indicates whether or not the propagator supports the trajectory of the moving object 
     * @param movingObject the moving object
     * @return returns boolean value
     */
    boolean supports(MovingObject movingObject);
}
