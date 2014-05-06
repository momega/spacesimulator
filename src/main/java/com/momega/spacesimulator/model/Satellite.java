package com.momega.spacesimulator.model;

/**
 * Created by martin on 5/5/14.
 */
public class Satellite extends DynamicalPoint {

    public Satellite(String name, Trajectory trajectory, Time time, double[] trajectoryColor) {
        super(name, trajectory, time, trajectoryColor);
    }
}
