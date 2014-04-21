package com.momega.spacesimulator.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class computer keplerian trajector on and object along the elipse. It computer only in 2D
 * Created by martin on 4/21/14.
 */
public class KeplerianTrajectory2d implements Trajectory {

    private static final Logger logger = LoggerFactory.getLogger(KeplerianTrajectory2d.class);

    private final double a;
    private final double epsilon;
    private final double period;
    private final double b; // the semiminor axis

    /**
     * The
     * @param a the semimajor axis
     * @param epsilon numberic eccentricity
     * @param period the orbital period
     */
    public KeplerianTrajectory2d(double a, double epsilon, double period) {

        this.a = a;
        this.epsilon = epsilon;
        this.period = period;

        this.b = a* Math.sqrt(1 - epsilon* epsilon);
    }

    @Override
    public Vector3d computePosition(double t) {
        double e = Math.sqrt(a*a - b*b); //  eccentricity
        double E = Math.PI; //  eccentric anomaly
        double M = 2 * Math.PI * t / period;   // mean anomaly
        M = normalAngle(M);

        double F = E - epsilon * Math.sin(M) - M;
        for(int i=0; i<50; i++) {
            E = E - F / (1.0f - epsilon * Math.cos(E));
            F = E - epsilon * Math.sin(E) - M;
//            if (F<MINOR_ERROR) {
//                break;
//            }
        }
        double x = (a * Math.cos(E)) - e;
        double y = (b * Math.sin(E));

        double cosTheta = (Math.cos(E) - epsilon) / ( 1 - epsilon * Math.cos(E));
        double theta;
        if (E < Math.PI) {
            theta = Math.acos(cosTheta);
        } else {
            theta = 2*Math.PI - Math.acos(cosTheta);
        }

        logger.info("" + t + "->[{},{}], theta:" + theta, x, y);

        return new Vector3d(x, y, 0);
    }

    public double normalAngle(double angle) {
        int z = (int)(angle / (2 * Math.PI));
        angle = angle - z * 2 * Math.PI;
        return angle;
    }
}
