package com.momega.spacesimulator.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class computer keplerian trajectory and object along the eclipse. It computes only in 2D
 * Created by martin on 4/21/14.
 */
public class KeplerianTrajectory2d implements Trajectory {

    private static final Logger logger = LoggerFactory.getLogger(KeplerianTrajectory2d.class);

    private static double MINOR_ERROR = Math.pow(10, -10);

    private final DynamicalPoint centralObject;
    private final double semimajorAxis; // (a)
    private final double eccentricity; // epsilon
    private final double timeOfPeriapsis;
    private final double period; // T
    private final double argumentOfPeriapsis; // lowercase omega

    private final double p; // semilatus rectum

    /**
     * Constructs the keplerian trajectory solver in 2D
     * @param semimajorAxis the semimajor axis
     * @param eccentricity numeric eccentricity
     * @param argumentOfPeriapsis argument of periapsis
     * @param period the orbital period
     * @param timeOfPeriapsis time of periapsis (Tp)
     */
    public KeplerianTrajectory2d(DynamicalPoint centralObject, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double timeOfPeriapsis) {
        this.centralObject = centralObject;
        this.semimajorAxis = semimajorAxis;
        this.eccentricity = eccentricity;
        this.timeOfPeriapsis = timeOfPeriapsis;
        this.argumentOfPeriapsis = argumentOfPeriapsis * Math.PI / 180;
        this.period = period;
        this.p = semimajorAxis* (1 - eccentricity* eccentricity);
    }

    /**
     * Computes the position of the object in 2D for the given time
     * @param t the time
     * @return new position of the object
     */
    @Override
    public Vector3d computePosition(double t) {
        double[] solution = solveKeplerian(t);
        double r = solution[0];
        double theta = solution[1];

        double x = centralObject.getPosition().x + r * Math.cos(theta + argumentOfPeriapsis);
        double y = centralObject.getPosition().y + r * Math.sin(theta + argumentOfPeriapsis);

        logger.debug("r = {}, theta = {}", r, theta);

        return new Vector3d(x, y, 0);
    }

    /**
     * Solves the keplerian problem for the given time
     * @param t the time
     * @return the array with the radius (r) and theta (real anomaly)
     */
    protected double[] solveKeplerian(double t) {
        double E = Math.PI; //  eccentric anomaly
        double M = 2 * Math.PI * (t - timeOfPeriapsis) / period;   // mean anomaly
        M = normalAngle(M);

        double F = E - eccentricity * Math.sin(M) - M;
        for(int i=0; i<50; i++) {
            E = E - F / (1.0 - eccentricity * Math.cos(E));
            F = E - eccentricity * Math.sin(E) - M;
            if (F<MINOR_ERROR) {
                break;
            }
        }

        double cosTheta = (Math.cos(E) - eccentricity) / ( 1.0 - eccentricity * Math.cos(E));
        double theta;
        if (E < Math.PI) {
            theta = Math.acos(cosTheta);
        } else {
            theta = 2*Math.PI - Math.acos(cosTheta);
        }

        double r = p / (1 + eccentricity * cosTheta);
        logger.debug("r = {}, theta = {}", r, theta);
        return new double[] {r, theta};
    }

    public double normalAngle(double angle) {
        int z = (int)(angle / (2 * Math.PI));
        angle = angle - z * 2 * Math.PI;
        return angle;
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
}