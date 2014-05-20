package com.momega.spacesimulator.model;

import org.joda.time.DateTime;

/**
 * The abstract implementation of the trajectory
 * Created by martin on 10.5.2014.
 */
public abstract class Trajectory {

    private double[] trajectoryColor;

    //TODO: remove this method to the service package
    public void initialize() {
        // ready for override
    }

    public double[] getTrajectoryColor() {
        return trajectoryColor;
    }

    public void setTrajectoryColor(double[] trajectoryColor) {
        this.trajectoryColor = trajectoryColor;
    }

    /**
     * Computes the position of and object in the time newTimestamp. The set new position, velocity and orientation
     * @param movingObject the moving objects
     * @param newTimestamp new timestamp
     */
    public abstract void computePosition(MovingObject movingObject, DateTime newTimestamp);
}
