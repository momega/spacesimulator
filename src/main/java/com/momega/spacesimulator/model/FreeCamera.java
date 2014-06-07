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

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }

}
