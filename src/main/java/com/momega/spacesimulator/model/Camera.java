package com.momega.spacesimulator.model;

/**
 * This class represents the camera. It is subclass od the 3d object
 *
 * Created by martin on 4/15/14.
 */
public class Camera extends Object3d {

    /**
     Constructs a new camera.
     @param position	The position of the camera
     @param nVector		The direction the camera is looking
     @param vVector		The "up" direction for the camera
     */
    public Camera(Vector3d position, Vector3d nVector, Vector3d vVector)
    {
        super(position, nVector, vVector);
    }

    public void move(double step) {
        moveN(step);
    }

    public void twist(double step) {
        rotate(getN(), step);
    }

    public void lookLeft(double step) {
        rotate(new Vector3d(0,0,1), step);
    }

    public void lookUp(double step) {
        rotate(getU(), step);
    }



}
