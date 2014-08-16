package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Vector3d;

/**
 * Created by martin on 8/16/14.
 */
public interface ForceModel {

    /**
     * Gets acceleration created by the a natural or artificial force
     * @param spacecraft the spacecraft
     * @param dt time interval
     * @return the acceleration vector
     */
    Vector3d getAcceleration(Spacecraft spacecraft, double dt);
}
