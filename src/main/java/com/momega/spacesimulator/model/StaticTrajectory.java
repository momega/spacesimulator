package com.momega.spacesimulator.model;

/**
 * The trajectory of the static object.
 * Created by martin on 4/21/14.
 */
public class StaticTrajectory extends Trajectory {

    private Vector3d position;

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public Vector3d getPosition() {
        return position;
    }

}
