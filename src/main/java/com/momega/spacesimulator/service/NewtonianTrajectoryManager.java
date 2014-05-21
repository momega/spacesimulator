package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by martin on 5/21/14.
 */
public class NewtonianTrajectoryManager implements TrajectoryManager {

    public static final double G = 6.67384*1E-11;

    private List<Planet> planets;

    @Override
    public void computePosition(MovingObject movingObject, DateTime newTimestamp) {
        Vector3d a = new Vector3d(0d, 0d, 0d);
        double dt = Time.getSeconds(newTimestamp, movingObject.getTimestamp());
        for(Planet planet : getPlanets()) {
            Vector3d r = planet.getPosition().clone().subtract(movingObject.getPosition());
            double dist3 = r.lengthSquared() * r.length();
            r.scale(G * planet.getMass() / dist3);  // a(i) = G*M * r(i) / r^3
            a.add(r);
        }
        Vector3d velocity = Vector3d.scaleAdd(dt, a, movingObject.getVelocity()); // velocity: v(i) = v(i) + a(i) * dt
        Vector3d position = Vector3d.scaleAdd(dt, velocity, movingObject.getPosition()); // position: r(i) = r(i) * v(i) * dt

        movingObject.setPosition(position);
        movingObject.setVelocity(velocity);
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
