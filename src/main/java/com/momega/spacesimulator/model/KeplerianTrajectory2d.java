package com.momega.spacesimulator.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class computer keplerian trajectory and object along the eclipse. It computes only in 2D
 * Created by martin on 4/21/14.
 */
public class KeplerianTrajectory2d implements Trajectory {

    private static final Logger logger = LoggerFactory.getLogger(KeplerianTrajectory2d.class);

    private final double semimajorAxis; // (a)
    private final double eccentricity; // epsilon
    private final double period; // T
    private final double argumentOfPeriapsis; // lowercase omega

    private final double p; // semilatus rectum

    /**
     * Constructs the keplerian trajectory solver in 2D
     * @param semimajorAxis the semimajor axis
     * @param eccentricity numeric eccentricity
     * @param period the orbital period
     * @param argumentOfPeriapsis argument of periapsis
     */
    public KeplerianTrajectory2d(double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period) {
        this.semimajorAxis = semimajorAxis;
        this.eccentricity = eccentricity;
        this.argumentOfPeriapsis = argumentOfPeriapsis;
        this.period = period;
        this.p = semimajorAxis* (1 - eccentricity* eccentricity);
    }

    @Override
    public Vector3d computePosition(double t) {
        double[] solution = solveKeplerian(t);
        double r = solution[0];
        double theta = solution[1];

        double x = r * Math.cos(theta + argumentOfPeriapsis);
        double y = r * Math.sin(theta + argumentOfPeriapsis);

        logger.debug("r = {}, theta = {}", r, theta);

        return new Vector3d(x, y, 0);
    }

    protected double[] solveKeplerian(double t) {
        double E = Math.PI; //  eccentric anomaly
        double M = 2 * Math.PI * t / period;   // mean anomaly
        M = normalAngle(M);

        double F = E - eccentricity * Math.sin(M) - M;
        for(int i=0; i<50; i++) {
            E = E - F / (1.0f - eccentricity * Math.cos(E));
            F = E - eccentricity * Math.sin(E) - M;
//            if (F<MINOR_ERROR) {
//                break;
//            }
        }

        double cosTheta = (Math.cos(E) - eccentricity) / ( 1 - eccentricity * Math.cos(E));
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
}
