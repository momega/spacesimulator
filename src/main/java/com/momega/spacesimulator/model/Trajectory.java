package com.momega.spacesimulator.model;

/**
 * Created by martin on 4/21/14.
 */
public interface Trajectory {

    /**
     * Computes the position of and object in the time t. The set new position, velocity and orientation
     * @param movingObject the moving objects
     * @param time the time
     * @return the position of an object
     */
    void computePosition(MovingObject movingObject, Time time);

    /**
     * Initializes the trajectory
     */
    void initialize();

    /**
     * The color of the trajectory
     * @return
     */
    double[] getTrajectoryColor();
}
