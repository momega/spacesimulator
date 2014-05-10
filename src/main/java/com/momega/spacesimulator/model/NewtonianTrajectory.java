package com.momega.spacesimulator.model;

import java.util.List;

/**
 * Created by martin on 5/5/14.
 */
public class NewtonianTrajectory extends AbstractTrajectory {

    public static final double G = 6.67384*1E-11;

    private List<Planet> planets;

    public void computePosition(MovingObject movingObject, Time time) {
        Vector3d a = new Vector3d(0d, 0d, 0d);
        for(Planet planet : planets) {
            Vector3d r = movingObject.getPosition().clone().subtract(planet.getPosition());
            double dist3 = r.lengthSquared() * r.length();
            r.scale(G * planet.getMass() / dist3);  // a(i) = G*M * r(i) / r^3
            a.add(r);
        }
        Vector3d velocity = Vector3d.scaleAdd(time.getWarpFactor(), a, movingObject.getVelocity()); // velocity: v(i) = v(i) + a(i) * dt
        Vector3d position = Vector3d.scaleAdd(time.getWarpFactor(), velocity, movingObject.getPosition()); // position: r(i) = r(i) * v(i) * dt

        movingObject.setPosition(position);
        movingObject.setVelocity(velocity);
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }
}
