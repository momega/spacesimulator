package com.momega.spacesimulator.model;

import com.momega.spacesimulator.model.Object3d;
import com.momega.spacesimulator.model.Vector3d;

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
    private Trajectory trajectory; // planets trajector

    /**
     Constructs a new camera.
     * @param position    The position of the camera
     * @param nVector        The direction the camera is looking
     * @param vVector        The "up" direction for the camera
     * @param trajectory planets trajectory
     * @param axialTilt axial tilt of the planet
     */
    public Planet(Vector3d position, Vector3d nVector, Vector3d vVector, Trajectory trajectory, double axialTilt, double radius, String textureFileName) {
        super(position, nVector, vVector);
        this.trajectory = trajectory;
        this.axialTilt = axialTilt;
        this.radius = radius;
        this.textureFileName = textureFileName;

        rotate(getU(), axialTilt);
    }

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
}
