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

//
//    private double solveTheta3(double E, double eccentricity) {
//        double param = Math.sqrt((1 + eccentricity) / (1 - eccentricity));
//        double theta = Math.atan(param * Math.sin(E) / (Math.cos(E) - eccentricity));
//        return theta;
//    }
//

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



//
//    public static double getAltitude(MovingObject movingObject) {
//        return getAltitude(movingObject.getKeplerianElements(), movingObject.getKeplerianElements().getTrueAnomaly());
//    }
//
//



}