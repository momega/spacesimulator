package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Computes the position of the {@link com.momega.spacesimulator.model.MovingObject} along the keplerian trajectory.
 * The manager does not compute the velocity
 * Created by martin on 5/21/14.
 */
@Component
public class KeplerianTrajectoryManager implements TrajectoryManager {

    private static double MINOR_ERROR = Math.pow(10, -12);
    private static final Logger logger = LoggerFactory.getLogger(KeplerianTrajectoryManager.class);

    @Override
    public void computePosition(MovingObject movingObject, Timestamp newTimestamp) {
        KeplerianElements keplerianElements = movingObject.getKeplerianElements();

        Vector3d[] result = solveKeplerian2(keplerianElements, newTimestamp);
        Vector3d r = result[0].add(keplerianElements.getCentralObject().getPosition());
        Vector3d v = result[1].add(keplerianElements.getCentralObject().getVelocity());

        movingObject.setPosition(r);
        movingObject.setVelocity(v);
    }

    @Override
    public boolean supports(Trajectory trajectory) {
        return TrajectoryType.KEPLERIAN.equals(trajectory.getType());
    }

    protected Vector3d[] solveKeplerian2(KeplerianElements keplerianElements, Timestamp time) {
        double E = solveEccentricAnomaly(keplerianElements, time);

        double theta = solveTheta1(E, keplerianElements.getEccentricity());
        keplerianElements.setTrueAnomaly(theta);

        double omega = keplerianElements.getArgumentOfPeriapsis();
        double OMEGA = keplerianElements.getAscendingNode();
        double i = keplerianElements.getInclination();
        double e = keplerianElements.getEccentricity();
        double a = keplerianElements.getSemimajorAxis();

        Vector3d P = new Vector3d(
                Math.cos(omega) * Math.cos(OMEGA) - Math.sin(omega) * Math.cos(i) * Math.sin(OMEGA),
                Math.cos(omega) * Math.sin(OMEGA) + Math.sin(omega) * Math.cos(i) * Math.cos(OMEGA),
                Math.sin(omega) * Math.sin(i)
                );

        Vector3d Q = new Vector3d(
                -Math.sin(omega) * Math.cos(OMEGA) - Math.cos(omega) * Math.cos(i) * Math.sin(OMEGA),
                - Math.sin(omega) * Math.sin(OMEGA) + Math.cos(omega) * Math.cos(i) * Math.cos(OMEGA),
                Math.cos(omega) * Math.sin(i)
        );

        Vector3d r = P.scale(a * (Math.cos(E) - e)).scaleAdd(a * Math.sqrt(1 - e*e) * Math.sin(E), Q);

        double n = 2 * Math.PI / keplerianElements.getPeriod().doubleValue();
        double derE = n / (1 - e* Math.cos(E));
        Vector3d v = P.scale(-a * Math.sin(E) * derE).scaleAdd(a * Math.sqrt(1 - e*e) * Math.cos(E) * derE, Q);

        return new Vector3d[] { r, v };
    }

    /**
     * Solves the keplerian problem for the given time
     * @param time the time
     * @return the array with the radius (r) and theta (real anomaly)
     */
    protected Vector3d[] solveKeplerian(KeplerianElements keplerianElements, Timestamp time) {

        double eccentricity = keplerianElements.getEccentricity();
        double p = keplerianElements.getSemimajorAxis() * (1 - eccentricity* eccentricity);

        double E = solveEccentricAnomaly(keplerianElements, time);
        double theta = solveTheta1(E, eccentricity);

        double r = p / (1 + eccentricity * Math.cos(theta));
        logger.debug("r = {}, theta = {}", r, theta);

        Vector3d position = MathUtils.getKeplerianPosition(keplerianElements, r, theta);
        return new Vector3d[] {position, new Vector3d(0d,0d,0d)};
    }

    protected double solveTheta1(double E, double eccentricity) {
        double cosTheta = (Math.cos(E) - eccentricity) / ( 1.0 - eccentricity * Math.cos(E));
        double theta;
        if (E < Math.PI) {
            theta = Math.acos(cosTheta);
        } else {
            theta = 2*Math.PI - Math.acos(cosTheta);
        }
        return theta;
    }

    protected double solveTheta2(double E, double eccentricity) {
        double param = Math.sqrt((1+eccentricity)/(1-eccentricity));
        double theta = 2*Math.atan(param * Math.tan(E/2));
        if (theta < 0) {
            theta = Math.PI * 2 + theta;
        }
        return theta;
    }

    protected double solveTheta3(double E, double eccentricity) {
        double param = Math.sqrt((1+eccentricity)/(1-eccentricity));
        double theta = Math.atan(param * Math.sin(E) / (Math.cos(E) - eccentricity) );
        return theta;
    }

    protected double solveEccentricAnomaly(KeplerianElements keplerianElements, Timestamp time) {
        logger.debug("time = {}", time);

        double E = Math.PI; //  eccentric anomaly
        double dt = TimeUtils.subtract(time, keplerianElements.getTimeOfPeriapsis()).getValue().doubleValue();
        double n = 2 * Math.PI / keplerianElements.getPeriod().doubleValue();
        double M = n * dt;   // mean anomaly
        M = MathUtils.normalizeAngle(M);

        logger.debug("M = {}", M);
        double eccentricity = keplerianElements.getEccentricity();
        double F = E - eccentricity * Math.sin(M) - M;
        for(int i=0; i<50; i++) {
            E = E - F / (1.0 - eccentricity * Math.cos(E));
            F = E - eccentricity * Math.sin(E) - M;
            if (Math.abs(F)<MINOR_ERROR) {
                break;
            }
        }

        return E;
    }

}
