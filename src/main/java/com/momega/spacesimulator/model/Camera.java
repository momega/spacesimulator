package com.momega.spacesimulator.model;

/**
 * This class represents the camera. It is subclass od the 3d object
 *
 * Created by martin on 4/15/14.
 */
public class Camera {

    private Vector3d position;
    private PositionProvider targetObject;
    private Orientation oppositeOrientation;
    private double distance;

    public PositionProvider getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(PositionProvider targetObject) {
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

    /**
     * Returns the position of the object
     * @return the [x,y,z] coordinates
     */
    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }
}
