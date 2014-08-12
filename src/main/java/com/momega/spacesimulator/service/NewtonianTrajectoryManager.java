package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

/**
 * Computes the next position and velocity of the {@link com.momega.spacesimulator.model.MovingObject} along Newtonian Trajectory. The
 * implementation can use either Euler's or Runge-Kutta's method to computer the next iteration of the velocity and position
 * Created by martin on 5/21/14.
 */
@Component
public class NewtonianTrajectoryManager implements TrajectoryManager {

    private static final Logger logger = LoggerFactory.getLogger(NewtonianTrajectoryManager.class);

    public static final double G = 6.67384*1E-11;

    private static double MINOR_ERROR = Math.pow(10, -12);
    private int maxHistory = 100000;

    @Autowired
    private SphereOfInfluenceService sphereOfInfluenceService;

    @Override
    public void computePosition(MovingObject movingObject, Timestamp newTimestamp) {

        double dt = TimeUtils.subtract(newTimestamp, movingObject.getTimestamp()).getValue().doubleValue();

        Vector3d velocity = movingObject.getVelocity();
        Vector3d position = movingObject.getPosition();

//        Vector3d[] result = rk4Solver(position, velocity, dt);
//        movingObject.setVelocity(result[0]);
//        movingObject.setPosition(result[1]);

        Vector3d[] result = eulerSolver(position, velocity, dt);
        movingObject.setVelocity(result[0]);
        movingObject.setPosition(result[1]);

        Assert.isInstanceOf(Satellite.class, movingObject, "predication of trajectory is supported only for satellites");
        Satellite satellite = (Satellite) movingObject;

        computePrediction(satellite, newTimestamp);
        computeApsides(satellite);
        updateHistory(satellite);
    }

    private void computeApsides(Satellite satellite) {
        KeplerianElements keplerianElements = satellite.getKeplerianElements();

        double rp = keplerianElements.getSemimajorAxis()* (1 - keplerianElements.getEccentricity());
        double ra = keplerianElements.getSemimajorAxis()* (1 + keplerianElements.getEccentricity());
        Double HA = keplerianElements.getHyperbolicAnomaly();

        SatelliteTrajectory satelliteTrajectory = (SatelliteTrajectory) satellite.getTrajectory();
        if (keplerianElements.getEccentricity()<1 || (keplerianElements.getEccentricity()>1 && HA!=null)) {
            Apsis periapsis = satelliteTrajectory.getPeriapsis();
            if (periapsis == null) {
                periapsis = new Apsis();
                periapsis.setName("Pe of " + satellite.getName());
                periapsis.setType(ApsisType.PERIAPSIS);
                satelliteTrajectory.setPeriapsis(periapsis);
            }
            periapsis.setPosition(MathUtils.getKeplerianPosition(keplerianElements, rp, 0d));
        } else {
            satelliteTrajectory.setPeriapsis(null);
        }

        if (keplerianElements.getEccentricity()<1) {
            Apsis apoapsis = satelliteTrajectory.getApoapsis();
            if (apoapsis == null) {
                apoapsis = new Apsis();
                apoapsis.setName("Ap of " + satellite.getName());
                apoapsis.setType(ApsisType.APOAPSIS);
                satelliteTrajectory.setApoapsis(apoapsis);
            }
            apoapsis.setPosition(MathUtils.getKeplerianPosition(keplerianElements, ra, Math.PI));
        } else {
            satelliteTrajectory.setApoapsis(null);
        }

    }

    private void updateHistory(Satellite satellite) {
        List<Vector3d> positions = satellite.getHistoryTrajectory().getPositions();
        if (positions.size()> maxHistory) {
            positions.remove(0);
        }
        positions.add(satellite.getPosition());
    }

    /**
     * Computes the prediction of the trajectory. Currently the supports work only for {@link com.momega.spacesimulator.model.Satellite}s.
     * @param satellite the satellite object which.
     * @param newTimestamp new timestamp
     */
    public void computePrediction(Satellite satellite, Timestamp newTimestamp) {
        SphereOfInfluence soi = sphereOfInfluenceService.findCurrentSoi(satellite);
        CelestialBody soiCelestialBody = soi.getBody();
        Vector3d position = satellite.getPosition().subtract(soiCelestialBody.getPosition());
        Vector3d velocity = satellite.getVelocity().subtract(soiCelestialBody.getVelocity());

        Vector3d hVector = position.cross(velocity);
        double h = hVector.length();
        double i = Math.acos(hVector.z / h);

        DynamicalPoint soiBody = soi.getBody();
        double mi = soiBody.getMass() * G;

        Vector3d eVector = velocity.cross(hVector).scale(1/mi).subtract(position.normalize());
        double e = eVector.length();

        logger.debug("e = {}", e);

        double a = h*h / (1- e*e) / mi;

        double OMEGA = 0d;
        double omega = 0d; // this is for circular, equatorial orbit
        double theta;

        if (i > MINOR_ERROR) {
            Vector3d nVector = new Vector3d(0, 0, 1).cross(hVector);
            double n = nVector.length();
            OMEGA = Math.acos(nVector.x / n);
            if (nVector.y < 0) {
                OMEGA = 2 * Math.PI - OMEGA;
            }

            if (e>MINOR_ERROR) {
                omega = Math.acos(nVector.dot(eVector) / n / e);
                if (eVector.z < 0) {
                    omega = 2 * Math.PI - omega;
                }

                theta = Math.acos(eVector.dot(position) / e / position.length());
                if (position.dot(velocity) <0) {
                    theta = 2* Math.PI - theta;
                }

            } else {
                theta = Math.acos(nVector.dot(position) / n / position.length());
                if (position.z<0) {
                    theta = 2* Math.PI - theta;
                }
            }

        } else {
            if (e>MINOR_ERROR) {
                omega = Math.acos(eVector.x / e);
                if (eVector.y < 0) {
                    omega = 2 * Math.PI - omega;
                }

                theta = Math.acos(eVector.dot(position) / e / position.length());
                if (position.dot(velocity) <0) {
                    theta = 2* Math.PI - theta;
                }

            } else {
                theta = Math.acos(position.x / position.length());
                if (position.y <0) {
                    theta = 2* Math.PI - theta;
                }
            }
        }

        logger.debug("theta = {}, inclination = {}", theta, i);

        KeplerianElements keplerianElements = satellite.getKeplerianElements();
        if (keplerianElements == null) {
            keplerianElements = new KeplerianElements();
        }
        if (keplerianElements.getCentralObject() != soiBody) {
            logger.info("changing soi to {} for satellite {}", soiBody.getName(), satellite.getName());
        }
        keplerianElements.setCentralObject(soiBody);
        keplerianElements.setInclination(i);
        keplerianElements.setEccentricity(e);
        keplerianElements.setSemimajorAxis(a);
        keplerianElements.setAscendingNode(OMEGA);
        keplerianElements.setArgumentOfPeriapsis(omega);
        keplerianElements.setTrueAnomaly(theta);

        double period = 0;
        if (e < 1) {  // TODO: add here MINOR_ERROR for e
            keplerianElements.setHyperbolicAnomaly(null);
            double EA = getEA(keplerianElements);
            keplerianElements.setEccentricAnomaly(EA);

            double nn = Math.sqrt(mi / (a*a*a));
            period = 2* Math.PI / nn;
            double T = newTimestamp.getValue().doubleValue() - (EA - e * Math.sin(EA)) / nn;
            keplerianElements.setTimeOfPeriapsis(TimeUtils.newTime(BigDecimal.valueOf(T)));

        } else {
            keplerianElements.setHyperbolicAnomaly(getHA(keplerianElements));
            keplerianElements.setEccentricAnomaly(null);

            double nn = Math.sqrt(-mi / (a*a*a)); // a < 0
            period = 2* Math.PI / nn;
        }

        keplerianElements.setPeriod(BigDecimal.valueOf(period));


        satellite.setRelativeVelocity(velocity);
        satellite.setRelativePosition(position);
        satellite.setKeplerianElements(keplerianElements);
    }

    protected double getEA(KeplerianElements keplerianElements) {
        double eccentricity = keplerianElements.getEccentricity();
        double theta = keplerianElements.getTrueAnomaly();
        double param = Math.sqrt((1+eccentricity)/(1-eccentricity));
        double EA = 2 * Math.atan(Math.tan(theta/2) / param);
        logger.debug("EA = {}", EA);
        return EA;
    }

    protected double getHA(KeplerianElements keplerianElements) {
        double theta = keplerianElements.getTrueAnomaly();
        double eccentricity = keplerianElements.getEccentricity();
        double sinH = (Math.sin(theta) * Math.sqrt(eccentricity*eccentricity -1)) / (1 + eccentricity * Math.cos(theta));
        double HA = MathUtils.asinh(sinH);
        logger.debug("HA = {}", HA);
        return HA;
    }

    /**
     * Solves the velocity and position by the simple Euler method
     * @param position the current position
     * @param velocity the current velocity
     * @param dt time interval
     * @return the position
     */
    protected Vector3d[] eulerSolver(Vector3d position, Vector3d velocity, double dt) {
        // Euler's method
        Vector3d acceleration = getAcceleration(position);
        velocity = velocity.scaleAdd(dt, acceleration); // velocity: v(i) = v(i) + a(i) * dt
        position = position.scaleAdd(dt, velocity); // position: r(i) = r(i) * v(i) * dt
        return new Vector3d[] {velocity, position};
    }

    /**
     * Solves the velocity and position by RK4 method (Runge-Kutta method, 4th order)
     * @param position the current position
     * @param velocity the current velocity
     * @param dt time interval
     * @return new position
     */
    protected Vector3d[] rk4Solver(Vector3d position, Vector3d velocity, double dt) {
        // k[i]v are velocities
        // k[i]x are position

        Vector3d k1v = getAcceleration(position).scale(dt);
        Vector3d k1x = velocity.scale(dt);
        Vector3d k2v = getAcceleration(position.scaleAdd(dt/2, k1x)).scale(dt);
        Vector3d k2x = velocity.scaleAdd(1.0/2, k1v).scale(dt);
        Vector3d k3v = getAcceleration(position.scaleAdd(dt/2, k2x)).scale(dt);
        Vector3d k3x = velocity.scaleAdd(1.0/2, k2v).scale(dt);
        Vector3d k4v = getAcceleration(position.scaleAdd(dt, k3x)).scale(dt);
        Vector3d k4x = velocity.scaleAdd(1.0, k3v).scale(dt);

        velocity = velocity.add(rk4(k1v, k2v, k3v, k4v));
        position = position.add(rk4(k1x, k2x, k3x, k4x));
        return new Vector3d[] {velocity, position};
    }

    protected Vector3d rk4(Vector3d u1, Vector3d u2, Vector3d u3, Vector3d u4) {
        return u1.scaleAdd(2, u2).scaleAdd(2, u3).add(u4).scale(1.0 / 6);
    }

    /**
     * Computes the total gravitational force (acceleration) from all celestial bodies in the system on the point defined
     * in the 3D.
     * @param position the given position vector
     * @return total acceleration/force
     */
    protected Vector3d getAcceleration(Vector3d position) {
        Vector3d a = Vector3d.ZERO;
        for(DynamicalPoint dp : ModelHolder.getModel().getDynamicalPoints()) {
            if (dp instanceof CelestialBody) {
                CelestialBody celestialBody = (CelestialBody) dp;
                Vector3d r = celestialBody.getPosition().subtract(position);
                double dist3 = r.lengthSquared() * r.length();
                a = a.scaleAdd(G * celestialBody.getMass() / dist3, r); // a(i) = a(i) + G*M * r(i) / r^3
            }
        }
        return a;
    }

    @Override
    public boolean supports(Trajectory trajectory) {
        return TrajectoryType.NEWTONIAN.equals(trajectory.getType());
    }

    public void setSphereOfInfluenceService(SphereOfInfluenceService sphereOfInfluenceService) {
        this.sphereOfInfluenceService = sphereOfInfluenceService;
    }

    public void setMaxHistory(int maxHistory) {
        this.maxHistory = maxHistory;
    }
}
