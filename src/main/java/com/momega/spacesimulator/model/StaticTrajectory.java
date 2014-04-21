package com.momega.spacesimulator.model;

/**
 * The trajectory of the static object. The {#computePosition} method return alwayas the same position.
 * Created by martin on 4/21/14.
 */
public class StaticTrajectory implements Trajectory {

    private Vector3d position;

    /**
     * Construct static trajector with the given position
     * @param position
     */
    public StaticTrajectory(Vector3d position) {
        this.position = position;
    }

    @Override
    public Vector3d computePosition(double t) {
        return position;
    }
}
