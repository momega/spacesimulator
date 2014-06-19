package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
        KeplerianTrajectory3d trajectory = (KeplerianTrajectory3d) movingObject.getTrajectory();

        Vector3d[] result = solveKeplerian2(trajectory, newTimestamp);
        Vector3d r = result[0].add(trajectory.getCentralObject().getPosition());
        Vector3d v = result[1].add(trajectory.getCentralObject().getVelocity());

        movingObject.setPosition(r);
        movingObject.setVelocity(v);
    }

    @Override
    public void computePrediction(MovingObject movingObject) {
        // do nothing
    }

    @Override
    public boolean supports(MovingObject movingObject) {
        Assert.notNull(movingObject);
        return movingObject.getTrajectory() instanceof KeplerianTrajectory3d;
    }

    protected Vector3d[] solveKeplerian2(KeplerianTrajectory3d trajectory, Timestamp time) {
        double E = solveEccentricAnomaly(trajectory, time);

        double omega = trajectory.getArgumentOfPeriapsis();
        double OMEGA = trajectory.getAscendingNode();
        double i = trajectory.getInclination();
        double e = trajectory.getEccentricity();
        double a = trajectory.getSemimajorAxis();

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

        Vector3d r = P.scale(a * (Math.cos(E) - e));
        r = Vector3d.scaleAdd(a * Math.sqrt(1 - e*e) * Math.sin(E), Q,  r);

        double n = 2 * Math.PI / trajectory.getPeriod().doubleValue();
        double derE = n / (1 - e* Math.cos(E));

        Vector3d v = P.scale(-a * Math.sin(E) * derE);
        v = Vector3d.scaleAdd(a * Math.sqrt(1 - e*e) * Math.cos(E) * derE, Q, v);

        return new Vector3d[] { r, v };
    }

    /**
     * Solves the keplerian problem for the given time
     * @param time the time
     * @return the array with the radius (r) and theta (real anomaly)
     */
    protected Vector3d[] solveKeplerian(KeplerianTrajectory3d trajectory, Timestamp time) {

        double eccentricity = trajectory.getEccentricity();
        double p = trajectory.getSemimajorAxis() * (1 - eccentricity* eccentricity);

        double E = solveEccentricAnomaly(trajectory, time);

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

        Vector3d position = MathUtils.getKeplerianPosition(trajectory, r, theta);
        return new Vector3d[] {position, new Vector3d(0d,0d,0d)};
    }

    protected double solveEccentricAnomaly(KeplerianTrajectory2d trajectory, Timestamp time) {
        logger.debug("time = {}", time);

        double E = Math.PI; //  eccentric anomaly
        double dt = TimeUtils.subtract(time, trajectory.getTimeOfPeriapsis()).getValue().doubleValue();
        double n = 2 * Math.PI / trajectory.getPeriod().doubleValue();
        double M = n * dt;   // mean anomaly
        M = MathUtils.normalizeAngle(M);

        logger.debug("M = {}", M);
        double eccentricity = trajectory.getEccentricity();
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
