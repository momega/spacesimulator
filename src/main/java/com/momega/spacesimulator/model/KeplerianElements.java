package com.momega.spacesimulator.model;

import java.math.BigDecimal;

/**
 * The class holding keplerian elements of the trajectory
 * Created by martin on 4/21/14.
 */
public class KeplerianElements {

    private DynamicalPoint centralObject;
    private double semimajorAxis; // (a)
    private double eccentricity; // epsilon
    private Timestamp timeOfPeriapsis; // seconds
    private BigDecimal period; // T in seconds
    private double argumentOfPeriapsis; // lowercase omega
    private double inclination; // i
    private double ascendingNode; // uppercase omega
    private double trueAnomaly; // theta;

    public double getSemimajorAxis() {
        return this.semimajorAxis;
    }

    /**
     * Get eccentricity of the trajectory
     * @return the eccentricity value
     */
    public double getEccentricity() {
        return this.eccentricity;
    }

    public double getArgumentOfPeriapsis() {
        return this.argumentOfPeriapsis;
    }

    public DynamicalPoint getCentralObject() {
        return centralObject;
    }

    public BigDecimal getPeriod() {
        return period;
    }

    public void setPeriod(BigDecimal period) {
        this.period = period;
    }

    public void setCentralObject(DynamicalPoint centralObject) {
        this.centralObject = centralObject;
    }

    public void setEccentricity(double eccentricity) {
        this.eccentricity = eccentricity;
    }

    public Timestamp getTimeOfPeriapsis() {
        return timeOfPeriapsis;
    }

    public void setTimeOfPeriapsis(Timestamp timeOfPeriapsis) {
        this.timeOfPeriapsis = timeOfPeriapsis;
    }

    public void setSemimajorAxis(double semimajorAxis) {
        this.semimajorAxis = semimajorAxis;
    }

    public void setArgumentOfPeriapsis(double argumentOfPeriapsis) {
        this.argumentOfPeriapsis = argumentOfPeriapsis;
    }

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
