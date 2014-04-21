package com.momega.spacesimulator.model;

/**
 * Created by martin on 4/21/14.
 */
public interface Trajectory {

    /**
     * Computes the position of and object in the time t
     * @param t the time
     * @return the position of an object
     */
    Vector3d computePosition(double t);
}
