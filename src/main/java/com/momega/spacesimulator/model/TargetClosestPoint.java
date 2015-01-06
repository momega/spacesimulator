package com.momega.spacesimulator.model;

/**
 * Created by martin on 12/28/14.
 */
public class TargetClosestPoint extends AbstractTargetOrbitalPoint {

    private double distance;
    private double error;

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }
}
