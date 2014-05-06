package com.momega.spacesimulator.model;

/**
 * The class represents the planet. It is the 3d object with texture and displayed as sphere
 * The planet is the sphere with the given radius
 *
 * Created by martin on 4/15/14.
 */
public class Planet extends DynamicalPoint {

    private double fi = 0; // in degrees
    private double radius; // radius of the planer in thousand kilometers
    private double axialTilt;  // axial tilt of the planet in degrees
    private final double rotationPeriod; // rotation period in days
    private final double mass;
    private String textureFileName;

    /**
     Constructs a new planet.
     * @param trajectory planets trajectory
     * @param time the initial time
     * @param axialTilt axial tilt of the planet in degrees
     * @param radius the radius of the planet
     * @param mass the mass of the object in 10E24 kg
     * @param rotationPeriod rotation period in days
     * @param textureFileName the name of the texture file name
     * @param trajectoryColor the color of trajectory
     */
    public Planet(String name, Trajectory trajectory, Time time, double axialTilt, double radius, double rotationPeriod, double mass, String textureFileName, double[] trajectoryColor) {
        super(name, trajectory, time, trajectoryColor);
        this.axialTilt = axialTilt;
        this.radius = radius;
        this.rotationPeriod = rotationPeriod;
        this.mass = mass;
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

    public void rotate(Time time) {
        double z = time.getJulianDay() / getRotationPeriod();
        z = z - Math.floor(z);
        this.fi = z * 360;
    }

    public double getFi() {
        return fi;
    }

    /**
     * Axial Tilt of the planet in degrees
     * @return the axial tilt in degrees
     */
    public double getAxialTilt() {
        return this.axialTilt;
    }

    public double getRotationPeriod() {
        return rotationPeriod;
    }

    public double getMass() {
        return mass;
    }
}
