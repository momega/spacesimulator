package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Vector3d;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The force model of the gravitation
 * Created by martin on 8/16/14.
 */
@Component
public class GravityModel implements ForceModel {

    @Autowired
    private ModelService modelService;

    /**
     * Computes the total gravitational force (acceleration) from all celestial bodies in the system on the point defined
     * in the 3D.
     * @param spacecraft the spacecraft
     * @return total acceleration/force
     */
    public Vector3d getAcceleration(Model model, Spacecraft spacecraft, double dt) {
        Vector3d position = spacecraft.getPosition();
        Vector3d a = Vector3d.ZERO;
        for(CelestialBody celestialBody : modelService.findAllCelestialBodies(model)) {
            Vector3d r = celestialBody.getCartesianState().getPosition().subtract(position);
            double dist3 = r.lengthSquared() * r.length();
            a = a.scaleAdd(celestialBody.getGravitationParameter() / dist3, r); // a(i) = a(i) + G*M * r(i) / r^3
        }
        return a;
    }

}
