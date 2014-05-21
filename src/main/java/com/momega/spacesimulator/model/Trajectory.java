package com.momega.spacesimulator.model;

import org.joda.time.DateTime;

/**
 * The abstract implementation of the trajectory
 * Created by martin on 10.5.2014.
 */
public abstract class Trajectory {

    private double[] trajectoryColor;

    public double[] getTrajectoryColor() {
        return trajectoryColor;
    }

    public void setTrajectoryColor(double[] trajectoryColor) {
        this.trajectoryColor = trajectoryColor;
    }

}
