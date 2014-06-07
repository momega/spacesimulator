package com.momega.spacesimulator.model;

import org.joda.time.Duration;

import java.math.BigDecimal;

/**
 * The class computer keplerian trajectory and object along the eclipse. It computes only in 2D
 * Created by martin on 4/21/14.
 */
public class KeplerianTrajectory2d extends Trajectory {

    private DynamicalPoint centralObject;
    private double semimajorAxis; // (a)
    private double eccentricity; // epsilon
    private BigDecimal timeOfPeriapsis; // seconds
    private Duration period; // T in seconds
    private double argumentOfPeriapsis; // lowercase omega

    public double getSemimajorAxis() {
        return this.semimajorAxis;
    }

    public double getEccentricity() {
        return this.eccentricity;
    }

    public double getArgumentOfPeriapsis() {
        return this.argumentOfPeriapsis;
    }

    public DynamicalPoint getCentralObject() {
        return centralObject;
    }

    public void setPeriod(Duration period) {
        this.period = period;
    }

    public Duration getPeriod() {
        return period;
    }

    public void setCentralObject(DynamicalPoint centralObject) {
        this.centralObject = centralObject;
    }

    public void setEccentricity(double eccentricity) {
        this.eccentricity = eccentricity;
    }

    public BigDecimal getTimeOfPeriapsis() {
        return timeOfPeriapsis;
    }

    public void setTimeOfPeriapsis(BigDecimal timeOfPeriapsis) {
        this.timeOfPeriapsis = timeOfPeriapsis;
    }

    public void setSemimajorAxis(double semimajorAxis) {
        this.semimajorAxis = semimajorAxis;
    }

    public void setArgumentOfPeriapsis(double argumentOfPeriapsis) {
        this.argumentOfPeriapsis = argumentOfPeriapsis;
    }
}
