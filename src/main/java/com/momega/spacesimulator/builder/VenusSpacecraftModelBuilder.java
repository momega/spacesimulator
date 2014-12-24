package com.momega.spacesimulator.builder;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.HabitableModule;
import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.Propulsion;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.SpacecraftSubsystem;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.utils.TimeUtils;

public class VenusSpacecraftModelBuilder extends MediumSolarSystemModelBuilder {

	protected void initTime() {
		model.setTime(TimeUtils.fromDateTime(new DateTime(2015, 5, 17, 13, 0,
				DateTimeZone.UTC)));
	}

	@Override
	public void initSpacecrafts() {
		CelestialBody earth = (CelestialBody) findMovingObject("Earth");
		CelestialBody venus = (CelestialBody) findMovingObject("Venus");

		Vector3d position = KeplerianOrbit.getCartesianPosition(
				500 * 1E3 + earth.getRadius(), 0, 0, Math.PI, 2d);
		Vector3d top = earth.getOrientation().getV();
		Vector3d velocity = position.normalize().cross(top).scale(8200d)
				.negate();
		List<SpacecraftSubsystem> subsystems = new ArrayList<>();
		Propulsion propulsion = new Propulsion();
		propulsion.setMass(29000);
		propulsion.setFuel(28000);
		propulsion.setMassFlow(5);
		propulsion.setSpecificImpulse(311);
		propulsion.setName("Main Engine");
		subsystems.add(propulsion);

		HabitableModule habitableModule = new HabitableModule();
		habitableModule.setCrewCapacity(1);
		habitableModule.setMass(1000);
		habitableModule.setName("Habitat");
		subsystems.add(habitableModule);

		Spacecraft spacecraft = createSpacecraft(earth, "Spacecraft 3",
				position, velocity, 3, new double[] { 1, 1, 0 }, subsystems);

		addManeuver(spacecraft, "V1", 0 * 60d, 900d, 1d, 0, Math.toRadians(-90));
		addManeuver(spacecraft, "V2", 17 * 60d, 900, 1d, 0, Math.toRadians(0));
		addManeuver(spacecraft, "V3", 130 * 60d, 1100, 1d, 0, Math.toRadians(0));
		addManeuver(spacecraft, "V4", 339 * 60d, 1575, 1d, 0, Math.toRadians(0));
		addManeuver(spacecraft, "V5", 29059 * 60d, 684, 1d, 0,	Math.toRadians(90));
		addManeuver(spacecraft, "V5", 170000 * 60d, 63, 1d, Math.toRadians(90),	Math.toRadians(10));
		addManeuver(spacecraft, "V5", 181970.362 * 60d, 50, 1d,	Math.toRadians(2), Math.toRadians(60));

		// addManeuver(spacecraft, "V4", 1830 * 60d, 900, 1d, 0,
		// Math.toRadians(0));
		// addManeuver(spacecraft, "V5", 881 * 60d, 1040, 1d, 0,
		// Math.toRadians(0));
		// addManeuver(spacecraft, "V6", 9319*60d, 525, 1d, 0,
		// Math.toRadians(90));

		setTarget(spacecraft, venus);
	}
}
