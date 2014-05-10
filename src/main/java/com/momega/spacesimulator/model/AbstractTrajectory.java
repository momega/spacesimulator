package com.momega.spacesimulator.model;

/**
 * The abstract implementation of the trajectory
 * Created by martin on 10.5.2014.
 */
public abstract class AbstractTrajectory implements Trajectory {

    private double[] trajectoryColor;

    @Override
    public void initialize() {
        // ready for override
    }

    @Override
    public double[] getTrajectoryColor() {
        return trajectoryColor;
    }

    public void setTrajectoryColor(double[] trajectoryColor) {
        this.trajectoryColor = trajectoryColor;
    }
}
