package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.MathUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class computer keplerian trajectory and object along the eclipse. It computes only in 2D
 * Created by martin on 4/21/14.
 */
public class KeplerianTrajectory2d extends Trajectory {

    private static final Logger logger = LoggerFactory.getLogger(KeplerianTrajectory2d.class);

    private static double MINOR_ERROR = Math.pow(10, -12);

    private DynamicalPoint centralObject;
    private double semimajorAxis; // (a)
    private double eccentricity; // epsilon
    private DateTime timeOfPeriapsis; // julian day
    private Duration period; // T in seconds
    private double argumentOfPeriapsis; // lowercase omega
    private double p; // semilatus rectum

    //TODO: remove this method to the service package
    public void initialize() {
        this.p = semimajorAxis* (1 - eccentricity* eccentricity);
        //this.thetaParam = Math.sqrt((1+eccentricity)/(1-eccentricity));
    }

    /**
     * Computes the position of the object in 2D for the given time
     * @param newTimestamp the time
     */
    @Override
    public void computePosition(MovingObject movingObject, DateTime newTimestamp) {
        double[] solution = solveKeplerian(Time.getSeconds(newTimestamp));
        double r = solution[0];
        double theta = solution[1];

        double x = centralObject.getPosition().x + r * Math.cos(theta + argumentOfPeriapsis);
        double y = centralObject.getPosition().y + r * Math.sin(theta + argumentOfPeriapsis);

        logger.debug("r = {}, theta = {}", r, theta);

        movingObject.setPosition(new Vector3d(x, y, 0));
    }

    /**
     * Solves the keplerian problem for the given time
     * @param t the time
     * @return the array with the radius (r) and theta (real anomaly)
     */
    protected double[] solveKeplerian(double t) {
        logger.debug("time = {}", t);

        double E = Math.PI; //  eccentric anomaly
        double M = 2 * Math.PI * (t - Time.getSeconds(timeOfPeriapsis)) / getPeriod().getStandardSeconds();   // mean anomaly
        M = MathUtils.normalizeAngle(M);

        logger.debug("M = {}", M);

        double F = E - eccentricity * Math.sin(M) - M;
        for(int i=0; i<50; i++) {
            E = E - F / (1.0 - eccentricity * Math.cos(E));
            F = E - eccentricity * Math.sin(E) - M;
            if (Math.abs(F)<MINOR_ERROR) {
                break;
            }
        }

        double cosTheta = (Math.cos(E) - eccentricity) / ( 1.0 - eccentricity * Math.cos(E));
        logger.debug("E = {}, cosTheta = {}", E, cosTheta);

        double theta;
        if (E < Math.PI) {
            theta = Math.acos(cosTheta);
        } else {
            theta = 2*Math.PI - Math.acos(cosTheta);
        }

//        double theta = 2*Math.atan(thetaParam * Math.tan(E/2));

        double r = p / (1 + eccentricity * Math.cos(theta));
        logger.debug("r = {}, theta = {}", r, theta);
        return new double[] {r, theta};
    }

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

    public void setTimeOfPeriapsis(DateTime timeOfPeriapsis) {
        this.timeOfPeriapsis = timeOfPeriapsis;
    }

    public DateTime getTimeOfPeriapsis() {
        return timeOfPeriapsis;
    }

    public void setSemimajorAxis(double semimajorAxis) {
        this.semimajorAxis = semimajorAxis;
    }

    public void setArgumentOfPeriapsis(double argumentOfPeriapsis) {
        this.argumentOfPeriapsis = argumentOfPeriapsis;
    }
}
