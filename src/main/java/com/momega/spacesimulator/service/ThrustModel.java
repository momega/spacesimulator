package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.springframework.stereotype.Component;

/**
 * The model of the propeller propulsion system
 * Created by martin on 8/16/14.
 */
@Component
public class ThrustModel implements ForceModel {

    @Override
    public Vector3d getAcceleration(Spacecraft spacecraft, double dt) {
        Propulsion propulsion = findPropulsion(spacecraft);
        if (propulsion == null) {
            return Vector3d.ZERO;
        }

        Maneuver maneuver = findManeuver(spacecraft);
        if (maneuver == null) {
            return Vector3d.ZERO;
        }

        double dm = propulsion.getMassFlow() * maneuver.getThrottle() * dt;
        if (dm < propulsion.getFuel()) {
            // there is no fuel left
            return Vector3d.ZERO;
        }

        double thrust = propulsion.getMassFlow() * maneuver.getThrottle() * propulsion.getSpecificImpulse() * MathUtils.G0;
        double a = thrust / spacecraft.getMass();

        Vector3d n = spacecraft.getCartesianState().getVelocity().normalize();
        Vector3d acceleration = n.scale(a);

        // decrease the mass of the spacecraft
        spacecraft.setMass( spacecraft.getMass() - dm);
        propulsion.setFuel( propulsion.getFuel() - dm);

        return acceleration;
    }

    protected Propulsion findPropulsion(Spacecraft spacecraft) {
        for(SpacecraftSubsystem subsystem : spacecraft.getSubsystems()) {
            if (subsystem instanceof Propulsion) {
                return (Propulsion) subsystem;
            }
        }
        return null;
    }

    protected Maneuver findManeuver(Spacecraft spacecraft) {
        Timestamp timestamp = ModelHolder.getModel().getTime();
        for(Maneuver maneuver : spacecraft.getManeuvers()) {
            if (TimeUtils.isTimestampInInterval(timestamp, maneuver)) {
                return maneuver;
            }
        }
        return null;
    }

}
