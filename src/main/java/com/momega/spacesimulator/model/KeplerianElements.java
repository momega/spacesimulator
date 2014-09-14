package com.momega.spacesimulator.model;

import java.math.BigDecimal;

/**
 * The class holding keplerian elements of the trajectory
 * Created by martin on 4/21/14.
 */
public class KeplerianElements {

    private MovingObject centralObject;
    private double semimajorAxis; // (a)
    private double eccentricity; // epsilon
    private Timestamp timeOfPeriapsis; // seconds
    private BigDecimal period; // T in seconds
    private double argumentOfPeriapsis; // lowercase omega
    private double inclination; // i
    private double ascendingNode; // uppercase omega
    private double trueAnomaly; // theta
    private Double hyperbolicAnomaly; // HA
    private Double eccentricAnomaly; //EA

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

    public MovingObject getCentralObject() {
        return centralObject;
    }

    public BigDecimal getPeriod() {
        return period;
    }

    public void setPeriod(BigDecimal period) {
        this.period = period;
    }

    public void setCentralObject(MovingObject centralObject) {
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
     * @return the true anomaly in radians
     */
    public double getTrueAnomaly() {
        return trueAnomaly;
    }

    public void setTrueAnomaly(double trueAnomaly) {
        this.trueAnomaly = trueAnomaly;
    }

    /**
     * The hyperbolic anomaly of the keplerian trajectory. It can be null for elliptic trajectories
     * @return HA
     */
    public Double getHyperbolicAnomaly() {
        return hyperbolicAnomaly;
    }

    public void setHyperbolicAnomaly(Double hyperbolicAnomaly) {
        this.hyperbolicAnomaly = hyperbolicAnomaly;
    }

    /**
     * The eccentric anomaly of the keplerian trajectory. It can be null for hyperbolic trajectories
     * @return EA
     */
    public Double getEccentricAnomaly() {
        return eccentricAnomaly;
    }

    public void setEccentricAnomaly(Double eccentricAnomaly) {
        this.eccentricAnomaly = eccentricAnomaly;
    }
}
