package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Computes the next position and velocity of the {@link com.momega.spacesimulator.model.MovingObject} along newtonian trajectory
 * Created by martin on 5/21/14.
 */
public class NewtonianTrajectoryManager implements TrajectoryManager {

    public static final double G = 6.67384*1E-11;

    private List<Planet> planets;

    @Override
    public void computePosition(MovingObject movingObject, DateTime newTimestamp) {

        double dt = Time.getSeconds(newTimestamp, movingObject.getTimestamp());

        Vector3d velocity = movingObject.getVelocity().clone();
        Vector3d position = movingObject.getPosition().clone();

//        double step = dt/8;
//        for(int i=0; i<4; i++) {
//            Vector3d[] result = rk4Solver(position, velocity, step);
//            movingObject.setVelocity(result[0]);
//            movingObject.setPosition(result[1]);
//        }

        Vector3d[] result = eulerSolver(position, velocity, dt);
        movingObject.setVelocity(result[0]);
        movingObject.setPosition(result[1]);
    }

    /**
     * Solves the velocity and positon by the simple Euler method
     * @param position
     * @param velocity
     * @param dt
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
     * @param position
     * @param velocity
     * @param dt
     * @return new position
     */
    protected Vector3d[] rk4Solver(Vector3d position, Vector3d velocity, double dt) {
        // k[i]v are velocities
        // k[i]x are position

        Vector3d k1v = getAcceleration(position).scale(dt);
        Vector3d k1x = velocity.scaled(dt);
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
        return u1.scaleAdd(2, u2).scaleAdd(2, u3).add(u4).scale(1.0/6);
    }

    protected Vector3d getAcceleration(Vector3d position) {
        Vector3d a = new Vector3d(0d, 0d, 0d);
        for(Planet planet : getPlanets()) {
            Vector3d r = planet.getPosition().clone().subtract(position);
            double dist3 = r.lengthSquared() * r.length();
            r.scale(G * planet.getMass() / dist3);  // a(i) = G*M * r(i) / r^3
            a.add(r);
        }
        return a;
    }

    @Override
    public boolean supports(Trajectory trajectory) {
        return trajectory instanceof NewtonianTrajectory;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    public List<Planet> getPlanets() {
        return planets;
    }


}
