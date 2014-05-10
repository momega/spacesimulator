package com.momega.spacesimulator.model;

/**
 * The abstract implementation of the trajectory
 * Created by martin on 10.5.2014.
 */
public abstract class Trajectory {

    private double[] trajectoryColor;

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
     * Computes the position of and object in the time t. The set new position, velocity and orientation
     * @param movingObject the moving objects
     * @param time the time
     * @return the position of an object
     */
    public abstract void computePosition(MovingObject movingObject, Time time);
}
