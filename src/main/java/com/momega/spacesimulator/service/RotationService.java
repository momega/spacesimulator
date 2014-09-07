package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.RotatingObject;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Rotation service is used to rotate the {@link com.momega.spacesimulator.model.RotatingObject} such as {@link com.momega.spacesimulator.model.CelestialBody}
 * Created by martin on 5/25/14.
 */
@Component
public class RotationService {

    private static final Logger logger = LoggerFactory.getLogger(RotationService.class);

    /**
     * Rotate the object along its axis
     * @param rotatingObject the rotating object, possibly planet or sun
     * @param newTime new time
     */
    public void rotate(RotatingObject rotatingObject, Timestamp newTime) {
        double dt = newTime.subtract(TimeUtils.JD2000).doubleValue();
        double phi = dt / rotatingObject.getRotationPeriod();
        phi = MathUtils.normalizeAngle(phi * 2 * Math.PI);
        phi += Math.PI/2; //TODO : why? because of texture?

        logger.debug("phi = {}", phi);

        rotatingObject.setPrimeMeridian(rotatingObject.getPrimeMeridianJd2000() + phi);
    }
}
