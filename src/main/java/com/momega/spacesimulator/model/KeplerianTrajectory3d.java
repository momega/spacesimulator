package com.momega.spacesimulator.model;

import org.joda.time.DateTime;

/**
 * The class computer keplerian trajectory and object along the eclipse. It computes in 3D
 * Created by martin on 4/22/14.
 */
public class KeplerianTrajectory3d extends KeplerianTrajectory2d {

    private double inclination; // i
    private double ascendingNode; // uppercase omega

    /**
     * The inclination in radian
     * @return returns the inclination of the keplerian 3d trajectory
     */
    public double getInclination() {
        return inclination;
    }

    public double getAscendingNode() {
        return ascendingNode;
    }

    public void setAscendingNode(double ascendingNode) {
        this.ascendingNode = ascendingNode;
    }

    public void setInclination(double inclination) {
        this.inclination = inclination;
    }
}
