package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Computes the position of the {@link com.momega.spacesimulator.model.MovingObject} along the keplerian trajectory.
 * The manager does not compute the velocity
 * Created by martin on 5/21/14.
 */
public class KeplerianTrajectoryManager implements TrajectoryManager {

    private static double MINOR_ERROR = Math.pow(10, -12);
    private static final Logger logger = LoggerFactory.getLogger(KeplerianTrajectoryManager.class);

    @Override
    public void computePosition(MovingObject movingObject, BigDecimal newTimestamp) {
        KeplerianTrajectory3d trajectory = (KeplerianTrajectory3d) movingObject.getTrajectory();
        double[] solution = solveKeplerian(trajectory, newTimestamp.doubleValue());
        double r = solution[0];
        double theta = solution[1];

        logger.debug("r = {}, theta = {}", r, theta);

        double u =  theta + trajectory.getArgumentOfPeriapsis();

        double x = trajectory.getCentralObject().getPosition().x + r * (Math.cos(u) * Math.cos(trajectory.getAscendingNode()) - Math.sin(u) * Math.cos(trajectory.getInclination()) * Math.sin(trajectory.getAscendingNode()));
        double y = trajectory.getCentralObject().getPosition().y + r * (Math.cos(u) * Math.sin(trajectory.getAscendingNode()) + Math.sin(u) * Math.cos(trajectory.getInclination()) * Math.cos(trajectory.getAscendingNode()));
        double z = trajectory.getCentralObject().getPosition().z + r * (Math.sin(u) * Math.sin(trajectory.getInclination()));

        movingObject.setPosition(new Vector3d(x, y, z));
    }

    @Override
    public boolean supports(Trajectory trajectory) {
        return trajectory instanceof KeplerianTrajectory3d;
    }

    /**
     * Solves the keplerian problem for the given time
     * @param t the time
     * @return the array with the radius (r) and theta (real anomaly)
     */
    protected double[] solveKeplerian(KeplerianTrajectory2d trajectory, double t) {
        logger.debug("time = {}", t);

        double E = Math.PI; //  eccentric anomaly
        double M = 2 * Math.PI * (t - TimeUtils.getSeconds(trajectory.getTimeOfPeriapsis())) / trajectory.getPeriod().getStandardSeconds();   // mean anomaly
        M = MathUtils.normalizeAngle(M);

        logger.debug("M = {}", M);
        double eccentricity = trajectory.getEccentricity();
        double p = trajectory.getSemimajorAxis() * (1 - eccentricity* eccentricity);
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

}
