package com.momega.spacesimulator.model;

/**
 * The class represents the planet. It is the 3d object with texture and displayed as shpere
 * The planet is the sphere with the given radius
 *
 * Created by martin on 4/15/14.
 */
public class Planet extends DynamicalPoint {

    private double fi = 0;
    private double radius;
    private double axialTilt;  // axial tilt of the planet
    private String textureFileName;

    /**
     Constructs a new planet.
     * @param trajectory planets trajectory
     * @param axialTilt axial tilt of the planet
     * @param radius the radius of the planet
     * @param textureFileName the name of the texture file name
     * @param trajectoryColor the color of trajectory
     */
    public Planet(String name, Trajectory trajectory, double axialTilt, double radius, String textureFileName, double[] trajectoryColor) {
        super(name, trajectory, trajectoryColor);
        this.axialTilt = axialTilt;
        this.radius = radius;
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

    public void rotate(double angle) {
        this.fi += angle;
    }

    public double getFi() {
        return fi;
    }

    public double getAxialTilt() {
        return this.axialTilt;
    }

}
