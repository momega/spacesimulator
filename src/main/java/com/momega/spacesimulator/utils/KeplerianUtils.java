package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * The class contains set of the methods for computing position and velocity from keplerian elements. The class
 * is thread safe.
 * Created by martin on 7/12/14.
 */
public final class KeplerianUtils {

    private static final Logger logger = LoggerFactory.getLogger(KeplerianUtils.class);

    private static KeplerianUtils instance = new KeplerianUtils();

    public static KeplerianUtils getInstance() {
        return instance;
    }

    private KeplerianUtils() {
        super();
    }

//    public CartesianState computePosition(KeplerianElements keplerianElements, Timestamp newTimestamp) {
//        CartesianState cartesianState = solveKeplerian(keplerianElements, newTimestamp);
//
//        return cartesianState;
//    }
//
//    protected CartesianState solveKeplerian(KeplerianElements keplerianElements, Timestamp time) {
//        double E = solveEccentricAnomaly(keplerianElements, time);
//        keplerianElements.setEccentricAnomaly(E);
//
//        double theta = solveTheta(E, keplerianElements.getEccentricity());
//        keplerianElements.setTrueAnomaly(theta);
//    }
//
//
//
//    public Timestamp timeToApsis(MovingObject movingObject, ApsisType apsisType) {
//        return timeToAngle(movingObject.getKeplerianElements(), movingObject.getTimestamp(), apsisType.getTrueAnomaly(), true);
//    }
//
//
//
//    public double solveEA(KeplerianElements keplerianElements) {
//    	return solveEA(keplerianElements.getEccentricity(), keplerianElements.getTrueAnomaly());
//    }
//

//

//
//    private double solveTheta2(double E, double eccentricity) {
//        double param = Math.sqrt((1 + eccentricity) / (1 - eccentricity));
//        double theta = 2 * Math.atan(param * Math.tan(E / 2));
//        if (theta < 0) {
//            theta = Math.PI * 2 + theta;
//        }
//        return theta;
//    }
//
//    public double solveThetaFromHA(double HA, double eccentricity) {
//        double param = Math.sqrt((eccentricity + 1) / (eccentricity -1));
//        double theta = 2 * Math.atan(param * Math.tanh(HA / 2));
//        if (theta < 0) {
//            theta = Math.PI * 2 + theta;
//        }
//        return theta;
//    }
//
//    private double solveTheta3(double E, double eccentricity) {
//        double param = Math.sqrt((1 + eccentricity) / (1 - eccentricity));
//        double theta = Math.atan(param * Math.sin(E) / (Math.cos(E) - eccentricity));
//        return theta;
//    }
//
//    /**
//     * Returns ETA time in seconds between current time and planned time of the orbital point
//     * @param orbitalPoint the {@link AbstractOrbitalPoint}
//     * @return the ETA in seconds
//     */
//    public static double getETA(OrbitalPositionProvider orbitalPoint) {
//    	MovingObject movingObject = orbitalPoint.getMovingObject();
//    	Timestamp current = movingObject.getTimestamp();
//    	Timestamp future = orbitalPoint.getTimestamp();
//    	return future.subtract(current).doubleValue();
//    }
//
//
//
//
//
//
//
//    public double solveHyperbolicAnomaly(KeplerianElements keplerianElements, Timestamp time) {
//        double M = solveMeanAnomaly(keplerianElements, time);
//        double HA = solveHyperbolicAnomaly(keplerianElements, M);
//        return HA;
//    }
//
//
//
//    public void updatePeriapsis(MovingObject movingObject) {
//    	Assert.notNull(movingObject);
//    	Assert.notNull(movingObject.getTrajectory());
//        Apsis periapsis = movingObject.getTrajectory().getPeriapsis();
//        if (periapsis == null) {
//            periapsis = createApsis(movingObject, ApsisType.PERIAPSIS);
//        }
//        updateApsis(movingObject, periapsis);
//    }
//
//    public void updateApoapsis(MovingObject movingObject) {
//    	Assert.notNull(movingObject);
//    	Assert.notNull(movingObject.getTrajectory());
//        Apsis apoapsis = movingObject.getTrajectory().getApoapsis();
//        if (apoapsis == null) {
//            apoapsis = createApsis(movingObject, ApsisType.APOAPSIS);
//        }
//        updateApsis(movingObject, apoapsis);
//    }
//
//    /**
//     * Creates the apsis object
//     * @param movingObject the moving object
//     * @param apsisType the type of the {@link com.momega.spacesimulator.model.Apsis}
//     * @return new instance of the apsis
//     */
//    protected Apsis createApsis(MovingObject movingObject, ApsisType apsisType) {
//        Assert.notNull(apsisType);
//        Assert.notNull(movingObject);
//        KeplerianTrajectory trajectory = movingObject.getTrajectory();
//        Apsis apsis = new Apsis();
//        apsis.setType(apsisType);
//        apsis.setName(apsisType.getShortcut() + " of " + movingObject.getName());
//        apsis.setKeplerianElements(movingObject.getKeplerianElements());
//        apsis.setVisible(movingObject instanceof Spacecraft);
//        apsis.setMovingObject(movingObject);
//        if (apsisType.equals(ApsisType.PERIAPSIS)) {
//            trajectory.setPeriapsis(apsis);
//        } else if (apsisType.equals(ApsisType.APOAPSIS)) {
//            trajectory.setApoapsis(apsis);
//        }
//        return apsis;
//    }
//
//    /**
//     * Updates the {@link com.momega.spacesimulator.model.Apsis} timespamp and position
//     * @param movingObject the moving object
//     * @param apsis the apsis
//     */
//    protected void updateApsis(MovingObject movingObject, Apsis apsis) {
//        Vector3d position = getCartesianPosition(movingObject.getKeplerianElements(), apsis.getType().getTrueAnomaly());
//        Timestamp timestamp = timeToApsis(movingObject, apsis.getType());
//
//        apsis.setPosition(position);
//        apsis.setTimestamp(timestamp);
//    }
//
//    public static double getAltitude(MovingObject movingObject) {
//        return getAltitude(movingObject.getKeplerianElements(), movingObject.getKeplerianElements().getTrueAnomaly());
//    }
//
//



}