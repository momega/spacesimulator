package com.momega.spacesimulator.model;

/**
 * Created by martin on 5/25/14.
 */
public class RotatingObject extends DynamicalPoint {

    private double rotationPeriod; // rotation period in seconds

    public void setRotationPeriod(double rotationPeriod) {
        this.rotationPeriod = rotationPeriod;
    }

    public double getRotationPeriod() {
        return rotationPeriod;
    }
}
