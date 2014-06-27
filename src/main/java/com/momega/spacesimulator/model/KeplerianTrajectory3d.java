package com.momega.spacesimulator.model;

/**
 * The class computer keplerian trajectory and object along the eclipse. It computes in 3D
 * Created by martin on 4/22/14.
 */
public class KeplerianTrajectory3d extends KeplerianTrajectory2d {

    private double inclination; // i
    private double ascendingNode; // uppercase omega

    private double trueAnomaly = 0;

    /**
     * The inclination in radian
     * @return returns the inclination of the keplerian 3d trajectory
     */
    public double getInclination() {
        return inclination;
    }

    /**
     * Gets the Ascending node (upper omega)
     * @return ascending node in radians
     */
    public double getAscendingNode() {
        return ascendingNode;
    }

    public void setAscendingNode(double ascendingNode) {
        this.ascendingNode = ascendingNode;
    }

    public void setInclination(double inclination) {
        this.inclination = inclination;
    }

    /**
     * Gets the true anomaly
     * @return the tru anomaly in radians
     */
    public double getTrueAnomaly() {
        return trueAnomaly;
    }

    public void setTrueAnomaly(double trueAnomaly) {
        this.trueAnomaly = trueAnomaly;
    }
}
