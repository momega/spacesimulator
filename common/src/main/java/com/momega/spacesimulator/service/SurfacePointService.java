/**
 * 
 */
package com.momega.spacesimulator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Orientation;
import com.momega.spacesimulator.model.SurfacePoint;
import com.momega.spacesimulator.model.Vector3d;

/**
 * The service computes the position of the surface points
 * @author martin
 */
@Component
public class SurfacePointService {
	
	private static final Logger logger = LoggerFactory.getLogger(SurfacePointService.class);
	
	private static double GROUD_DISTANCE = 5000d;

	public void updateSurfacePoints(CelestialBody celestialBody) {
		for(SurfacePoint surfacePoint : celestialBody.getSurfacePoints()) {
			updateSurfacePoint(celestialBody, surfacePoint);
		}
	}
	
	protected void updateSurfacePoint(CelestialBody celestialBody, SurfacePoint surfacePoint) {
		Vector3d position = computePosition(celestialBody, surfacePoint);
		surfacePoint.setPosition(position);
	}
	
    protected Vector3d computePosition(CelestialBody celestialBody, SurfacePoint surfacePoint) {
        Vector3d position = celestialBody.getPosition();
        Orientation o = celestialBody.getOrientation().clone();
        o.rotate(o.getV(), celestialBody.getPrimeMeridian() + Math.PI / 2);
        o.rotate(o.getV(), surfacePoint.getCoordinates().getPhi());
        o.rotate(o.getU(), - Math.PI/2 + surfacePoint.getCoordinates().getTheta() );
        Vector3d n = o.getN();
        position = position.scaleAdd(celestialBody.getRadius()+GROUD_DISTANCE, n);
        return position;
    }

}
