package com.momega.spacesimulator.model;

/**
 * The rotation object is the {@link com.momega.spacesimulator.model.DynamicalPoint} with defined rotation period. The {@link CelestialBody}
 * is the typical subclass of the rotation object
 * Created by martin on 5/25/14.
 */
public class RotatingObject extends DynamicalPoint {

    private double rotationPeriod; // rotation period in seconds
    private double radius;

    public void setRotationPeriod(double rotationPeriod) {
        this.rotationPeriod = rotationPeriod;
    }

    public double getRotationPeriod() {
        return rotationPeriod;
    }

    /**
     * Gets the radius in meters of the planet
     * @return the value of the radius
     */
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
