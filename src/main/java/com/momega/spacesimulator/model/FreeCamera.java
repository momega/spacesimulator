package com.momega.spacesimulator.model;

/**
 * Created by martin on 5/8/14.
 */
public class FreeCamera extends Camera {

    /**
     * The velocity of the camera
     */
    private double velocity;

    public FreeCamera() {
    }

    /**
     * Moves the camera forward or backward with the current velocity
     * @param direction the direction
     *
     * //TODO: remove this method to the service package
     */
    public void moveByVelocity(double direction) {
        moveN(getVelocity() * direction);
    }

    //TODO: remove this method to the service package
    public void changeVelocity(double factor) {
        setVelocity(getVelocity() * factor);
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }

    //TODO: remove this method to the service package
    public void updatePosition() {
        // do nothing
    }
}
