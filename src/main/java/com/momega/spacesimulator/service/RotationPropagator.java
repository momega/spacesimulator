package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.RotatingObject;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Rotation propagator is used to 
 * rotate the {@link com.momega.spacesimulator.model.RotatingObject} such as {@link com.momega.spacesimulator.model.CelestialBody}.
 * It supports only RotatingObject.
 * Created by martin on 5/25/14.
 */
@Component
public class RotationPropagator implements Propagator {

    private static final Logger logger = LoggerFactory.getLogger(RotationPropagator.class);

	@Override
	public void computePosition(MovingObject movingObject, Timestamp newTimestamp) {
		if (ModelHolder.getModel().isRunningHeadless()) {
			return;
		}
		RotatingObject rotatingObject = (RotatingObject) movingObject;
		double dt = newTimestamp.subtract(TimeUtils.JD2000);
        double phi = dt / rotatingObject.getRotationPeriod();
        phi = MathUtils.normalizeAngle(phi * 2 * Math.PI);
        phi += Math.PI/2; //TODO : why? because of texture?

        logger.debug("phi = {}", phi);

        rotatingObject.setPrimeMeridian(rotatingObject.getPrimeMeridianJd2000() + phi);
	}

	@Override
	public boolean supports(MovingObject movingObject) {
		return movingObject instanceof RotatingObject;
	}
}
