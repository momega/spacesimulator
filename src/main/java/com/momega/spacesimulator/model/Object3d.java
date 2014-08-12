package com.momega.spacesimulator.model;

/**
 * This class represents a 3d object. It is defined with the position as {@link com.momega.spacesimulator.model.Vector3d} and
 * its {@link com.momega.spacesimulator.model.Orientation}.
 */
public class Object3d {

    private Vector3d position;

    /**
     * Returns the position of the object
     * @return the [x,y,z] coordinates
     */
    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

}