/**
 * 
 */
package com.momega.spacesimulator.json;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.KeplerianTrajectory;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.UserOrbitalPoint;

/**
 * @author martin
 *
 */
@Component
public class MovingObjectSerializer extends AbstractSerializer<MovingObject> {

	public MovingObjectSerializer() {
		super(MovingObject.class);
	}

	@Override
	public void write(JsonObject object, MovingObject value, Gson gson) {
		// do nothing
	}

	@Override
	public void read(JsonObject object, MovingObject value, Gson gson) {
		// relink orbital points
		for(UserOrbitalPoint userOrbitalPoint : value.getUserOrbitalPoints()) {
			userOrbitalPoint.setMovingObject(value);
		}
		KeplerianTrajectory trajectory = value.getTrajectory();
		if (trajectory != null) {
			Apsis apsis = trajectory.getApoapsis();
			if (apsis != null) {
				apsis.setMovingObject(value);
			}
			apsis = trajectory.getPeriapsis();
			if (apsis != null) {
				apsis.setMovingObject(value);
			}
		}
	}
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return null;
	}

}
