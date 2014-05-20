package com.momega.spacesimulator.model;

/**
 * The class represents the planet. It is the 3d object with texture and displayed as sphere
 * The planet is the sphere with the given radius
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
    public void rotate(Time time) {
        double z = time.getSeconds() / getRotationPeriod();
        z = z - Math.floor(z);
        //getOrientation().
        //getOrientation().lookLeft(z * 360);
    }

    @Override
    public void move(Time time) {
        super.move(time);
        rotate(time);
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
