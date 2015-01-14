package com.momega.spacesimulator.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;
import com.momega.spacesimulator.model.*;

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
		for(HistoryPoint historyPoint : value.getNamedHistoryPoints()) {
			historyPoint.setSpacecraft(value);
		}
		Target target = value.getTarget();
		if (target != null) {
			for(OrbitIntersection oi : target.getOrbitIntersections()) {
				oi.setMovingObject(value);
			}
		}
		if (value.getExitSoiOrbitalPoint()!=null) {
			value.getExitSoiOrbitalPoint().setMovingObject(value);
			if (value.getExitSoiOrbitalPoint().getClosestPoint()!=null) {
				value.getExitSoiOrbitalPoint().getClosestPoint().setMovingObject(value);
			}
		}
	}
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return null;
	}

}
