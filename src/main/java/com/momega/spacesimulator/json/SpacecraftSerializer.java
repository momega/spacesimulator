package com.momega.spacesimulator.json;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.OrbitIntersection;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Target;

@Component
public class SpacecraftSerializer extends AbstractSerializer<Spacecraft> {

	public SpacecraftSerializer() {
		super(Spacecraft.class);
	}

	@Override
	public void write(JsonObject object, Spacecraft value, Gson gson) {
		// do nothing
	}

	@Override
	public void read(JsonObject object, Spacecraft value, Gson gson) {
		for(Maneuver maneuver : value.getManeuvers()) {
			maneuver.getStart().setMovingObject(value);
			maneuver.getEnd().setMovingObject(value);
		}
		Target target = value.getTarget();
		if (target != null) {
			for(OrbitIntersection oi : target.getOrbitIntersections()) {
				oi.setMovingObject(value);
			}
		}
	}
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return null;
	}

}
