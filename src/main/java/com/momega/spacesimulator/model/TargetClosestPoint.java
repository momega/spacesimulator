package com.momega.spacesimulator.model;

/**
 * Created by martin on 12/28/14.
 */
public class TargetClosestPoint extends AbstractOrbitalPoint {

    private transient MovingObject targetObject;
    private double distance;
    private double error;

    public MovingObject getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(MovingObject targetObject) {
        this.targetObject = targetObject;
    }

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
