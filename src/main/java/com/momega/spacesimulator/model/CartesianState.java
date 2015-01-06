package com.momega.spacesimulator.model;

import org.apache.commons.math3.util.FastMath;

import com.momega.spacesimulator.utils.MathUtils;

/**
 * Created by martin on 8/12/14.
 */
public class CartesianState {

    private final static double MINOR_ERROR = Math.pow(10, -12);

    private Vector3d position;
    private Vector3d velocity;

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public Vector3d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
    }

    public CartesianState add(CartesianState other) {
        CartesianState result = new CartesianState();
        result.setPosition(getPosition().add(other.getPosition()));
        result.setVelocity(getVelocity().add(other.getVelocity()));
        return result;
    }

    public CartesianState subtract(CartesianState other) {
        CartesianState result = new CartesianState();
        result.setPosition(getPosition().subtract(other.getPosition()));
        result.setVelocity(getVelocity().subtract(other.getVelocity()));
        return result;
    }

    /**
     * Computes angular momentum
     * @return the angular momentum
     */
    public Vector3d getAngularMomentum() {
        return position.cross(velocity);
    }

    /**
     * Computes the keplerian elements from the cartesian coordinates.
     * @param soiBody the central object of the result central object. This comes from from sphere of influence
     * @param newTimestamp new timestamp
     * @return new instance of the keplerian elements.
     */
    public KeplerianElements toKeplerianElements(CelestialBody soiBody, Timestamp newTimestamp) {
        Vector3d position = getPosition();
        Vector3d velocity = getVelocity();
        Vector3d hVector = getAngularMomentum();

        double h = hVector.length();
        double i = FastMath.acos(hVector.getZ() / h);

        double mi = soiBody.getMass() * MathUtils.G;

        Vector3d eVector = velocity.cross(hVector).scale(1/mi).subtract(position.normalize());
        double e = eVector.length();

        double a = h*h / (1- e*e) / mi;

        double OMEGA = 0d;
        double omega = 0d; // this is for circular, equatorial orbit
        double theta;

        if (i > MINOR_ERROR) {
            Vector3d nVector = new Vector3d(0, 0, 1).cross(hVector);
            double n = nVector.length();
            OMEGA = FastMath.acos(nVector.getX() / n);
            if (nVector.getY() < 0) {
                OMEGA = 2 * Math.PI - OMEGA;
            }

            if (e>MINOR_ERROR) {
                omega = FastMath.acos(nVector.dot(eVector) / n / e);
                if (eVector.getZ() < 0) {
                    omega = 2 * Math.PI - omega;
                }

                double thetaCos = eVector.dot(position) / position.length() / e;
                if (thetaCos<-1) {
                	thetaCos = -1;
                } else if (thetaCos > 1) {
                	thetaCos = 1;
                }
                theta = FastMath.acos(thetaCos);
                
                if (position.dot(velocity) <0) {
                    theta = 2* Math.PI - theta;
                }

            } else {
                theta = FastMath.acos(nVector.dot(position) / n / position.length());
                if (position.getZ()<0) {
                    theta = 2* Math.PI - theta;
                }
            }

        } else {
            if (e>MINOR_ERROR) {
                omega = FastMath.acos(eVector.getX() / e);
                if (eVector.getY() < 0) {
                    omega = 2 * Math.PI - omega;
                }

                theta = FastMath.acos(eVector.dot(position) / e / position.length());
                if (position.dot(velocity) <0) {
                    theta = 2* Math.PI - theta;
                }

            } else {
                theta = FastMath.acos(position.getX() / position.length());
                if (position.getY() <0) {
                    theta = 2* Math.PI - theta;
                }
            }
        }

        KeplerianElements keplerianElements = new KeplerianElements();
        KeplerianOrbit keplerianOrbit = new KeplerianOrbit();
        keplerianElements.setKeplerianOrbit(keplerianOrbit);
        keplerianOrbit.setCentralObject(soiBody);
        keplerianOrbit.setInclination(i);
        keplerianOrbit.setEccentricity(e);
        keplerianOrbit.setSemimajorAxis(a);
        keplerianOrbit.setAscendingNode(OMEGA);
        keplerianOrbit.setArgumentOfPeriapsis(omega);
        keplerianElements.setTrueAnomaly(theta);
        
        double meanMotion;
        if (keplerianOrbit.isHyperbolic()) {
            meanMotion = FastMath.sqrt(-mi / (a * a * a));
        } else {
            meanMotion = FastMath.sqrt(mi / (a * a * a));
        }
        double period = 2* Math.PI / meanMotion;
        keplerianOrbit.setMeanMotion(Double.valueOf(meanMotion));
        keplerianOrbit.setPeriod(period);

        Timestamp TT = keplerianElements.timeToAngle(newTimestamp, 0.0, false);
        keplerianOrbit.setTimeOfPeriapsis(TT);

        return keplerianElements;
    }
    
    /**
     * Computers the relative keplerian elements of this cartesian state relative to some celestial body and
     * its current/future cartesian state
     * @param celestialBody
     * @param celestialBodyCartesianState
     * @param timestamp the timestamp
     * @return new instance of the keplerian elements
     */
    public KeplerianElements computeRelativeKeplerianElements(CelestialBody celestialBody, CartesianState celestialBodyCartesianState, Timestamp timestamp) {
    	CartesianState relativeCartesianState = subtract(celestialBodyCartesianState);
    	KeplerianElements relativeKeplerianElements = relativeCartesianState.toKeplerianElements(celestialBody, timestamp);
    	return relativeKeplerianElements;
    }

}
