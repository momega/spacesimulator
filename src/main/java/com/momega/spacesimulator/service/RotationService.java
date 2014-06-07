package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.RotatingObject;

import java.math.BigDecimal;

/**
 * Rotation service is used to rotate the {@link com.momega.spacesimulator.model.RotatingObject} such as {@link com.momega.spacesimulator.model.Planet}
 * Created by martin on 5/25/14.
 */
public class RotationService {

    /**
     * Rotate the object along its axis
     * @param rotatingObject the rotating object, possibly planet or sun
     * @param newTimestamp new timestamp
     */
    public void rotate(RotatingObject rotatingObject, BigDecimal newTimestamp) {
        double dt = newTimestamp.subtract(rotatingObject.getTimestamp()).doubleValue();
        double phi = dt/ rotatingObject.getRotationPeriod();
        phi *= (2*Math.PI);
        rotatingObject.getOrientation().lookLeft(phi);
    }
}
