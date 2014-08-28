package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import org.springframework.stereotype.Component;

/**
 * The force model of the gravitation
 * Created by martin on 8/16/14.
 */
@Component
public class GravityModel implements ForceModel {

    /**
     * Computes the total gravitational force (acceleration) from all celestial bodies in the system on the point defined
     * in the 3D.
     * @param spacecraft the spacecraft
     * @return total acceleration/force
     */
    public Vector3d getAcceleration(Spacecraft spacecraft, double dt) {
        Vector3d position = spacecraft.getPosition();
        Vector3d a = Vector3d.ZERO;
        for(MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
            if (dp instanceof CelestialBody) {
                CelestialBody celestialBody = (CelestialBody) dp;
                Vector3d r = celestialBody.getCartesianState().getPosition().subtract(position);
                double dist3 = r.lengthSquared() * r.length();
                a = a.scaleAdd(MathUtils.G * celestialBody.getMass() / dist3, r); // a(i) = a(i) + G*M * r(i) / r^3
            }
        }
        return a;
    }

}
