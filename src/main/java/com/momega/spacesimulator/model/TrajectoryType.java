package com.momega.spacesimulator.model;

/**
 * The type of the trajectory. This enum indicates which {@link com.momega.spacesimulator.service.Propagator}
 * will be used to calculate the new position, velocity and all keplerian elements for the {@link com.momega.spacesimulator.model.MovingObject}
 * Created by martin on 6/27/14.
 */
public enum TrajectoryType {
    KEPLERIAN,
    NEWTONIAN,
    STATIC
}
