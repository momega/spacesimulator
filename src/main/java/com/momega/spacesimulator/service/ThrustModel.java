package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import com.momega.spacesimulator.utils.VectorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The model of the propeller propulsion system
 * Created by martin on 8/16/14.
 */
@Component
public class ThrustModel implements ForceModel {

    private static final Logger logger = LoggerFactory.getLogger(ThrustModel.class);

    @Autowired
    private HistoryPointService historyPointService;

    @Override
    public Vector3d getAcceleration(Spacecraft spacecraft, double dt) {
        Propulsion propulsion = findPropulsion(spacecraft);

        if (propulsion == null) {
            // no propulsion installed in the spacecraft
            return Vector3d.ZERO;
        }

        Maneuver currentManeuver = spacecraft.getCurrentManeuver();
        Maneuver maneuver = findManeuver(spacecraft);
        if (maneuver == null) {
            endOfManeuver(spacecraft, currentManeuver);
            return Vector3d.ZERO;
        }

        double dm = propulsion.getMassFlow() * maneuver.getThrottle() * dt;
        if (dm > propulsion.getFuel()) {
            endOfManeuver(spacecraft, currentManeuver);
            return Vector3d.ZERO;
        }

        if (currentManeuver == null) {
            // start of the burn
            historyPointService.startManeuver(spacecraft, maneuver);
            spacecraft.setCurrentManeuver(maneuver);
        }

        double thrust = propulsion.getMassFlow() * maneuver.getThrottle() * propulsion.getSpecificImpulse() * MathUtils.G0;
        double a = thrust / spacecraft.getMass();

        if (logger.isDebugEnabled()) {
        	logger.debug("Engine is running, thrust = {}", thrust);
        }

        Orientation o = VectorUtils.rotateByAngles(spacecraft.getOrientation(), maneuver.getThrottleAlpha(), maneuver.getThrottleDelta());
        Vector3d n = o.getN();
        Vector3d acceleration = n.scale(a);
        spacecraft.setThrust(acceleration);

        // decrease the mass of the spacecraft
        //TODO: place this to different method, because of Runge-Kutta method
        spacecraft.setMass( spacecraft.getMass() - dm);
        propulsion.setFuel( propulsion.getFuel() - dm);

        if (logger.isDebugEnabled()) {
        	logger.debug("fuel level = {}, acc = {}", propulsion.getFuel(), acceleration);
        }

        return acceleration;
    }

    protected void endOfManeuver(Spacecraft spacecraft, Maneuver maneuver) {
        if (maneuver != null) {
        	spacecraft.setThrust(null);
            spacecraft.setCurrentManeuver(null);
            historyPointService.endManeuver(spacecraft, maneuver);
        }
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
