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
    private String textureFileName;


    /**
     Constructs a new camera.

     @param position	The position of the camera
     @param nVector		The direction the camera is looking
     @param vVector		The "up" direction for the camera
     */
    public Planet(Vector3d position, Vector3d nVector, Vector3d vVector, double radius, String textureFileName) {
        super(position, nVector, vVector);
        this.radius = radius;
        this.textureFileName = textureFileName;
    }

    public double getRadius() {
        return radius;
    }

    public String getTextureFileName() {
        return textureFileName;
    }

    public void rotate(float angle) {
        this.fi += angle;
    }

    public double getFi() {
        return fi;
    }
}
