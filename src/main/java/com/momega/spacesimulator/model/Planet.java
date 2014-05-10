package com.momega.spacesimulator.model;

/**
 * The class represents the planet. It is the 3d object with texture and displayed as sphere
 * The planet is the sphere with the given radius
 *
 * Created by martin on 4/15/14.
 */
public class Planet extends DynamicalPoint {

    private double radius; // radius of the planer in thousand kilometers
    private double rotationPeriod; // rotation period in days
    private String textureFileName;

    /**
     * Gets the radius of the planet
     * @return the value of the radius
     */
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setRotationPeriod(double rotationPeriod) {
        this.rotationPeriod = rotationPeriod;
    }

    public String getTextureFileName() {
        return textureFileName;
    }

    public void setTextureFileName(String textureFileName) {
        this.textureFileName = textureFileName;
    }

    public void rotate(Time time) {
        // TODO: fix this
//        double z = time.getJulianDay() / getRotationPeriod();
//        z = z - Math.floor(z);
//        this.fi = z * 360;
    }

    /**
     * Sets the axial tilt of the planet
     * @param angle the angle
     */
    public void setAxialTitle(double angle) {
        getOrientation().lookUp(angle);
    }

    public double getRotationPeriod() {
        return rotationPeriod;
    }

    public double getMass() {
        return mass;
    }
}
