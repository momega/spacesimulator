package com.momega.spacesimulator.model;

/**
 * The class represents the planet. It is the 3d object with texture and displayed as shpere
 * The planet is the sphere with the given radius
 *
 * Created by martin on 4/15/14.
 */
public class Planet extends Object3d {

    private double fi = 0;
    private double radius;
    private double axialTilt;  // axial tilt of the planet
    private String textureFileName;
    private double[] trajectoryColor;
    private Trajectory trajectory; // planets trajector

    /**
     Constructs a new camera.
     * @param nVector The direction the camera is looking
     * @param vVector The "up" direction for the camera
     * @param trajectory planets trajectory
     * @param axialTilt axial tilt of the planet
     * @param radius the radius of the planet
     * @param textureFileName the name of the texture file name
     * @param trajectoryColor the color of trajectory
     */
    public Planet(Vector3d nVector, Vector3d vVector, Trajectory trajectory, double axialTilt, double radius, String textureFileName, double[] trajectoryColor) {
        super(trajectory.computePosition(0), nVector, vVector);
        this.trajectory = trajectory;
        this.axialTilt = axialTilt;
        this.radius = radius;
        this.textureFileName = textureFileName;
        this.trajectoryColor = trajectoryColor;
        rotate(getU(), axialTilt);
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

    /**
     * Computers the planet position along the defined trajectory in the given time t
     * @param t time
     */
    public void move(double t) {
        this.position = trajectory.computePosition(t);
    }

    public Trajectory getTrajectory() {
        return this.trajectory;
    }

    public double[] getTrajectoryColor() {
        return trajectoryColor;
    }
}
