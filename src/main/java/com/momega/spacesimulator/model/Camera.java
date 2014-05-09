package com.momega.spacesimulator.model;

/**
 * This class represents the camera. It is subclass od the 3d object
 *
 * Created by martin on 4/15/14.
 */
public class Camera extends Object3d {

    private double velocity;

    /**
     Constructs a new camera.
     @param position	The position of the camera
     @param nVector		The direction the camera is looking
     @param vVector		The "up" direction for the camera
     */
    public Camera(Vector3d position, Vector3d nVector, Vector3d vVector, double velocity) {
        super(position, new Orientation(nVector, vVector));
        this.velocity = velocity;
    }

    public void move(double step) {
        moveN(step);
    }

    /**
     * Moves the camera forward or backward with the current velocity
     * @param direction the direction
     */
    public void moveByVelocity(double direction) {
        move(getVelocity() * direction);
    }

    public void changeVelocity(double factor) {
        setVelocity(getVelocity() * factor);
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }
}
