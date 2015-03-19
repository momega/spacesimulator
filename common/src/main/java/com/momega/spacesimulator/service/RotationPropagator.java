package com.momega.spacesimulator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.RotatingObject;
import com.momega.spacesimulator.model.RunStep;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * Rotation propagator is used to 
 * rotate the {@link com.momega.spacesimulator.model.RotatingObject} such as {@link com.momega.spacesimulator.model.CelestialBody}.
 * It supports only RotatingObject.
 * Created by martin on 5/25/14.
 */
@Component
public class RotationPropagator implements Propagator {
    
    @Autowired
    private SurfacePointService surfacePointService;	

    private static final Logger logger = LoggerFactory.getLogger(RotationPropagator.class);

	@Override
	public void computePosition(Model model, MovingObject movingObject, RunStep step) {
		if (step.isRunningHeadless()) {
			return;
		}
		RotatingObject rotatingObject = (RotatingObject) movingObject;
		double dt = step.getNewTimestamp().subtract(TimeUtils.JD2000);
        double phi = dt / rotatingObject.getRotationPeriod() * 2 * Math.PI;
        phi = MathUtils.normalizeAngle(phi);

        logger.debug("phi = {}", phi);

        rotatingObject.setPrimeMeridian(rotatingObject.getPrimeMeridianJd2000() + phi);
        
        if (movingObject instanceof CelestialBody) {
    		CelestialBody celestialBody = (CelestialBody) movingObject;
    		surfacePointService.updateSurfacePoints(celestialBody);
    	}        
	}

	@Override
	public boolean supports(MovingObject movingObject) {
		return movingObject instanceof RotatingObject;
	}
}
