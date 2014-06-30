package com.momega.spacesimulator.model;

/**
 * This class represents the camera. It is subclass od the 3d object
 *
 * Created by martin on 4/15/14.
 */
public class Camera extends Object3d {

    private Object3d targetObject;
    private Orientation oppositeOrientation;
    private double distance;

    public Object3d getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Object3d targetObject) {
        this.targetObject = targetObject;
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
