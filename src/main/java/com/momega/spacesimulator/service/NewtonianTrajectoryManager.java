package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.TimeUtils;
import org.springframework.util.Assert;

/**
 * Computes the next position and velocity of the {@link com.momega.spacesimulator.model.MovingObject} along {@link com.momega.spacesimulator.model.NewtonianTrajectory}. The
 * implementation can use either Euler's or Runge-Kutta's method to computer the next iteration of the velocity and position
 * Created by martin on 5/21/14.
 */
public class NewtonianTrajectoryManager implements TrajectoryManager {

    public static final double G = 6.67384*1E-11;

    private static double MINOR_ERROR = Math.pow(10, -12);

    private UniverseService universeService;
    private SphereOfInfluenceService sphereOfInfluenceService;

    @Override
    public void computePosition(MovingObject movingObject, Timestamp newTimestamp) {

        double dt = TimeUtils.subtract(newTimestamp, movingObject.getTimestamp()).getValue().doubleValue();

        Vector3d velocity = movingObject.getVelocity();
        Vector3d position = movingObject.getPosition();

        Vector3d[] result = rk4Solver(position, velocity, dt);
        movingObject.setVelocity(result[0]);
        movingObject.setPosition(result[1]);

//        Vector3d[] result = eulerSolver(position, velocity, dt);
//        movingObject.setVelocity(result[0]);
//        movingObject.setPosition(result[1]);

        if (movingObject instanceof Satellite) {
            Satellite satellite = (Satellite) movingObject;
            Planet soiPlanet = satellite.getSphereOfInfluence().getBody();
            satellite.setRelativePosition(movingObject.getPosition().subtract(soiPlanet.getPosition()));
            satellite.setRelativeVelocity(movingObject.getVelocity().subtract(soiPlanet.getVelocity()));
        }
    }

    /**
     * Computes the prediction of the trajectory. Currently the supports work only for {@link com.momega.spacesimulator.model.Satellite}s.
     * @param movingObject the moving object which.
     */
    @Override
    public void computePrediction(MovingObject movingObject) {
        Assert.isInstanceOf(Satellite.class, movingObject, "predication of trajectory is supported only for satellites");

        Satellite satellite = (Satellite) movingObject;
        SphereOfInfluence soi = sphereOfInfluenceService.findCurrentSoi(satellite);
        sphereOfInfluenceService.updateRelativePositionAndVelocity(satellite);
        Vector3d position = satellite.getRelativePosition();
        Vector3d velocity = satellite.getRelativeVelocity();

        Vector3d hVector = position.cross(velocity);
        double h = hVector.length();
        double i = Math.acos(hVector.z / h);

        DynamicalPoint soiBody = soi.getBody();
        double mi = soiBody.getMass() * G;

        Vector3d eVector = velocity.cross(hVector).scale(1/mi).subtract(position.normalize());
        double e = eVector.length();

        double a = h*h / ( 1- e*e) / mi;

        double OMEGA = 0;
        double omega = 0;

        if (i > MINOR_ERROR) {
            Vector3d nVector = new Vector3d(0, 0, 1).cross(hVector);
            double n = nVector.length();
            OMEGA = Math.acos(nVector.x / n);
            if (nVector.y < 0) {
                OMEGA = 2 * Math.PI - OMEGA;
            }

            omega = Math.acos( nVector.dot(eVector) / n / e);
            if (eVector.z <0) {
                omega = 2 * Math.PI - omega;
            }
        } else {
            omega = Math.acos(eVector.x / e);
            if (eVector.y<0) {
                omega = 2 * Math.PI - omega;
            }
        }

        KeplerianTrajectory3d prediction = new KeplerianTrajectory3d();
        prediction.setCentralObject(soiBody);
        prediction.setInclination(i);
        prediction.setEccentricity(e);
        prediction.setSemimajorAxis(a);
        prediction.setAscendingNode(OMEGA);
        prediction.setArgumentOfPeriapsis(omega);
        prediction.setTrajectoryColor(new double[] {1d, 1d, 0d});

        NewtonianTrajectory nt = (NewtonianTrajectory) movingObject.getTrajectory();
        nt.setPrediction(prediction);
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
        Vector3d k2v = getAcceleration(Vector3d.scaleAdd(dt/2, k1x, position)).scale(dt);
        Vector3d k2x = Vector3d.scaleAdd(1.0/2, k1v, velocity).scale(dt);
        Vector3d k3v = getAcceleration(Vector3d.scaleAdd(dt/2, k2x, position)).scale(dt);
        Vector3d k3x = Vector3d.scaleAdd(1.0/2, k2v, velocity).scale(dt);
        Vector3d k4v = getAcceleration(Vector3d.scaleAdd(dt, k3x, position)).scale(dt);
        Vector3d k4x = Vector3d.scaleAdd(1.0, k3v, velocity).scale(dt);

        velocity = velocity.add(rk4(k1v, k2v, k3v, k4v));
        position = position.add(rk4(k1x, k2x, k3x, k4x));
        return new Vector3d[] {velocity, position};
    }

    protected Vector3d rk4(Vector3d u1, Vector3d u2, Vector3d u3, Vector3d u4) {
        return u1.scaleAdd(2, u2).scaleAdd(2, u3).add(u4).scale(1.0 / 6);
    }

    protected Vector3d getAcceleration(Vector3d position) {
        Vector3d a = new Vector3d();
        for(Planet planet : universeService.getPlanets()) {
            Vector3d r = planet.getPosition().subtract(position);
            double dist3 = r.lengthSquared() * r.length();
            a = a.scaleAdd(G * planet.getMass() / dist3, r); // a(i) = a(i) + G*M * r(i) / r^3
        }
        return a;
    }

    @Override
    public boolean supports(MovingObject movingObject) {
        Assert.notNull(movingObject);
        return (movingObject instanceof  Satellite) && (movingObject.getTrajectory() instanceof NewtonianTrajectory);
    }

    public void setUniverseService(UniverseService universeService) {
        this.universeService = universeService;
    }

    public void setSphereOfInfluenceService(SphereOfInfluenceService sphereOfInfluenceService) {
        this.sphereOfInfluenceService = sphereOfInfluenceService;
    }
}
