package com.momega.spacesimulator.model;

/**
 * Created by martin on 5/8/14.
 */
public class AttachedCamera extends Camera {

    private DynamicalPoint dynamicalPoint;
    private double distance;

    public AttachedCamera() {
    }

    @Override
    public void updatePosition() {
        setPosition(Vector3d.scaleAdd(distance, new Vector3d(1d, 0d, 0d), getDynamicalPoint().getPosition()));
    }

    public DynamicalPoint getDynamicalPoint() {
        return dynamicalPoint;
    }

    public void setDynamicalPoint(DynamicalPoint dynamicalPoint) {
        this.dynamicalPoint = dynamicalPoint;
    }

    public void changeDistance(double factor) {
        distance = distance * factor;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
