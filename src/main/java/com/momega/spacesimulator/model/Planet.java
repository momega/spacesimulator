package com.momega.spacesimulator.model;

import org.joda.time.DateTime;

/**
 * The class represents the planet. It is the dynamical point with defined texture and rotation period
 *
 * Created by martin on 4/15/14.
 */
public class Planet extends DynamicalPoint {

    private double rotationPeriod; // rotation period in seconds
    private String textureFileName;

    public void setRotationPeriod(double rotationPeriod) {
        this.rotationPeriod = rotationPeriod;
    }

    public String getTextureFileName() {
        return textureFileName;
    }

    public void setTextureFileName(String textureFileName) {
        this.textureFileName = textureFileName;
    }

    //TODO: remove this method to the service package
    public void rotate(DateTime newTimestamp) {
        double phi = Time.getSeconds(newTimestamp, getTimestamp())/ getRotationPeriod();
        phi *= (2*Math.PI);
        getOrientation().lookLeft(phi);
    }

    @Override
    public void move(DateTime newTimestamp) {
        rotate(newTimestamp);
        super.move(newTimestamp);
    }

    /**
     * Sets the axial tilt of the planet
     * @param angle the angle in radians
     */
    //TODO: remove this method to the service package
    public void setAxialTilt(double angle) {
        getOrientation().lookUp(angle);
    }

    public double getRotationPeriod() {
        return rotationPeriod;
    }

}
