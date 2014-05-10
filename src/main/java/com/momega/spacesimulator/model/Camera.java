package com.momega.spacesimulator.model;

/**
 * This class represents the camera. It is subclass od the 3d object
 *
 * Created by martin on 4/15/14.
 */
public class Camera extends Object3d {

    private double velocity;

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
