package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.RotatingObject;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.TimeUtils;
import com.momega.spacesimulator.utils.VectorUtils;
import org.springframework.stereotype.Component;

/**
 * Rotation service is used to rotate the {@link com.momega.spacesimulator.model.RotatingObject} such as {@link com.momega.spacesimulator.model.Planet}
 * Created by martin on 5/25/14.
 */
@Component
public class RotationService {

    /**
     * Rotate the object along its axis
     * @param rotatingObject the rotating object, possibly planet or sun
     * @param newTime new time
     */
    public void rotate(RotatingObject rotatingObject, Timestamp newTime) {
        double dt = TimeUtils.subtract(newTime, rotatingObject.getTimestamp()).getValue().doubleValue();
        double phi = dt / rotatingObject.getRotationPeriod();
        phi *= (2*Math.PI);
        VectorUtils.lookLeft(rotatingObject.getOrientation(), phi);
    }
}
