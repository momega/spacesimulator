package com.momega.spacesimulator.model;

/**
 * The class represents the planet. It is the 3d object with texture and displayed as sphere
 * The planet is the sphere with the given radius
 *
 * Created by martin on 4/15/14.
 */
public class Planet extends DynamicalPoint {

    private double fi = 0; // in degrees
    private double radius;
    private double axialTilt;  // axial tilt of the planet in degrees
    private final double rotationPeriod;
    private String textureFileName;

    /**
     Constructs a new planet.
     * @param trajectory planets trajectory
     * @param axialTilt axial tilt of the planet in degrees
     * @param radius the radius of the planet
     * @param rotationPeriod rotation period in days
     * @param textureFileName the name of the texture file name
     * @param trajectoryColor the color of trajectory
     */
    public Planet(String name, Trajectory trajectory, double axialTilt, double radius, double rotationPeriod, String textureFileName, double[] trajectoryColor) {
        super(name, trajectory, trajectoryColor);
        this.axialTilt = axialTilt;
        this.radius = radius;
        this.rotationPeriod = rotationPeriod;
        this.textureFileName = textureFileName;
        getObject().rotate(getObject().getV(), axialTilt);
    }

    /**
     * Gets the radius of the planet
     * @return the value of the radius
     */
    public double getRadius() {
        return radius;
    }

    public String getTextureFileName() {
        return textureFileName;
    }

    public void rotate(double dt) {
        this.fi += ((dt / rotationPeriod) * 360);
    }

    public double getFi() {
        return fi;
    }

    public double getAxialTilt() {
        return this.axialTilt;
    }

}
