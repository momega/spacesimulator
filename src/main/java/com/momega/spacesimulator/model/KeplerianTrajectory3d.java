package com.momega.spacesimulator.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class computer keplerian trajector and object along the elipse. It computes in 3D
 * Created by martin on 4/22/14.
 */
public class KeplerianTrajectory3d extends KeplerianTrajectory2d {

    private double inclination; // i
    private double ascendingNode; // uppercase omega

    public KeplerianTrajectory3d(double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double inclination, double ascendingNode) {
        super(semimajorAxis, eccentricity, argumentOfPeriapsis, period);
        this.inclination = inclination;
        this.ascendingNode = ascendingNode;
    }

    public double getInclination() {
        return inclination;
    }

    public double getAscendingNode() {
        return ascendingNode;
    }

    @Override
    public Vector3d computePosition(double t) {
        double[] solution = solveKeplerian(t);
        double r = solution[0];
        double theta = solution[1];

        double x = r * (Math.cos(theta + getArgumentOfPeriapsis()) * Math.cos(ascendingNode) - Math.cos(inclination) * Math.sin(theta + getArgumentOfPeriapsis()) * Math.sin(ascendingNode));
        double y = r * (Math.sin(theta + getArgumentOfPeriapsis()) * Math.sin(ascendingNode) + Math.cos(inclination) * Math.sin(theta + getArgumentOfPeriapsis()) * Math.cos(ascendingNode));
        double z = r * (Math.sin(theta + getArgumentOfPeriapsis()) * Math.sin(inclination));

        return new Vector3d(x, y, z);
    }
}
