package com.momega.spacesimulator.model;

import java.util.List;

/**
 * Created by martin on 5/5/14.
 */
public class NewtonianTrajectory implements Trajectory {

    public static final double G24 = 6.67384*1E-11 * 1E24;

    private final List<Planet> planets;
    private Object3d object;

    public NewtonianTrajectory(List<Planet> planets, Object3d object) {
        this.planets = planets;
        this.object = object;
    }

    @Override
    public Vector3d computePosition(Time time) {
        Vector3d a = new Vector3d(0d, 0d, 0d);
        for(Planet planet : planets) {
            Vector3d r = this.object.getPosition().clone().subtract(planet.getPosition());
            r.scale(1E6);
            double dist3 = r.lengthSquared() * r.length();
            r.scale(G24 * planet.getMass() / dist3);  // a(i) = G*M * r(i) / r^3
            a.add(r);
        }
        Vector3d velocity = Vector3d.scaleAdd(time.getWarpFactor(), a, this.object.getN()); // velocity v(i) = v(i) + a(i) * dt
        return Vector3d.scaleAdd(time.getWarpFactor(), velocity, this.object.getPosition());
    }

}
