package com.momega.spacesimulator.model;

/**
 * Created by martin on 5/8/14.
 */
public class AttachedCamera extends Camera {

    private DynamicalPoint dynamicalPoint;
    private double distance;

    public DynamicalPoint getDynamicalPoint() {
        return dynamicalPoint;
    }

    public void setDynamicalPoint(DynamicalPoint dynamicalPoint) {
        this.dynamicalPoint = dynamicalPoint;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
