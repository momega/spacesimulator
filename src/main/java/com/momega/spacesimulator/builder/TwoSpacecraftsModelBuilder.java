package com.momega.spacesimulator.builder;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * Created by martin on 10/19/14.
 */
public class TwoSpacecraftsModelBuilder extends MediumSolarSystemModelBuilder {
	
	protected void initTime() {
        model.setTime(TimeUtils.fromDateTime(new DateTime(2016, 2, 22, 10, 0, DateTimeZone.UTC)));
        model.setWarpFactor(BigDecimal.ONE);
    }

    @Override
    public void initSpacecrafts() {
        CelestialBody earth = (CelestialBody) findMovingObject("Earth");
        CelestialBody mars = (CelestialBody) findMovingObject("Mars");

        Vector3d position = KeplerianOrbit.getCartesianPosition(500 * 1E3 + earth.getRadius(), 0, 0, Math.PI, 2d);
        Vector3d top = earth.getOrientation().getV();
        Vector3d velocity = position.normalize().cross(top).scale(8200d).negate();
        Spacecraft spacecraft = createSpacecraft(earth, "Spacecraft 2", position, velocity, 2);

        Propulsion propulsion = new Propulsion();
        propulsion.setMass(29000);
        propulsion.setFuel(28000);
        propulsion.setMassFlow(5);
        propulsion.setSpecificImpulse(311);
        propulsion.setName("Main Engine");
        addSpacecraftSubsystem(spacecraft, propulsion);

        HabitableModule habitableModule = new HabitableModule();
        habitableModule.setCrewCapacity(1);
        habitableModule.setMass(1000);
        habitableModule.setName("Habitat");
        addSpacecraftSubsystem(spacecraft, habitableModule);

        addManeuver(spacecraft, "M5", 0 * 60d, 1100d, 1d, 0, Math.toRadians(-90));
        addManeuver(spacecraft, "M6", 82 * 60d, 1200d, 1d, 0, Math.toRadians(-90));
        addManeuver(spacecraft, "M7", 107.5 * 60d, 2654d, 1d, 0, Math.toRadians(0));
        addManeuver(spacecraft, "M8", 116107 * 60d, 261d, 1d, 0, Math.toRadians(90));
        addManeuver(spacecraft, "M9", 264824 * 60d, 4, 1d, 0, Math.toRadians(90));
        addManeuver(spacecraft, "M10", 308000 * 60d, 96, 1d, Math.toRadians(90), Math.toRadians(45));
        addManeuver(spacecraft, "M11", 308100 * 60d, 40, 1d, Math.toRadians(45), Math.toRadians(-30));
        addManeuver(spacecraft, "M12", 310495 * 60d, 500, 1d, Math.toRadians(180), Math.toRadians(0));

        setTarget(spacecraft, mars);

        addMovingObject(spacecraft);

    }
}
