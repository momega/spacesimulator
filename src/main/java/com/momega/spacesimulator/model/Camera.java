package com.momega.spacesimulator.model;

/**
 * This class represents the camera. It is subclass od the 3d object
 *
 * Created by martin on 4/15/14.
 */
public class Camera extends Object3d {

    private DynamicalPoint dynamicalPoint;
    private Orientation oppositeOrientation;
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

    public void setOppositeOrientation(Orientation oppositeOrientation) {
        this.oppositeOrientation = oppositeOrientation;
    }

    public Orientation getOppositeOrientation() {
        return oppositeOrientation;
    }
}
