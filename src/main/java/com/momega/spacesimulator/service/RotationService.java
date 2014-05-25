package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.RotatingObject;
import com.momega.spacesimulator.model.Time;
import org.joda.time.DateTime;

/**
 * Created by martin on 5/25/14.
 */
public class RotationService {

    /**
     * Rotate the object along its axis
     * @param rotatingObject the rotating object, possibly planet or sun
     * @param newTimestamp new timestamp
     */
    public void rotate(RotatingObject rotatingObject, DateTime newTimestamp) {
        double phi = Time.getSeconds(newTimestamp, rotatingObject.getTimestamp())/ rotatingObject.getRotationPeriod();
        phi *= (2*Math.PI);
        rotatingObject.getOrientation().lookLeft(phi);
    }
}
