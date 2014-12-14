package com.momega.spacesimulator.builder;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.HabitableModule;
import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.Propulsion;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.utils.TimeUtils;

public class VenusSpacecraftModelBuilder extends MediumSolarSystemModelBuilder {

	protected void initTime() {
        model.setTime(TimeUtils.fromDateTime(new DateTime(2015, 6, 6, 14, 0, DateTimeZone.UTC)));
        model.setWarpFactor(BigDecimal.ONE);
    }

    @Override
    public void initSpacecrafts() {
        CelestialBody earth = (CelestialBody) findMovingObject("Earth");
        CelestialBody venus = (CelestialBody) findMovingObject("Venus");

        Vector3d position = KeplerianOrbit.getCartesianPosition(500 * 1E3 + earth.getRadius(), 0, 0, Math.PI, 2d);
        Vector3d top = earth.getOrientation().getV();
        Vector3d velocity = position.normalize().cross(top).scale(8200d).negate();
        Spacecraft spacecraft = createSpacecraft(earth, "Spacecraft 3", position, velocity, 3, new double[] {1, 1, 0});

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
        
        addManeuver(spacecraft, "V1", 0* 60d, 900d, 1d, 0, Math.toRadians(-90));
        addManeuver(spacecraft, "V2", 85 * 60d, 700d, 1d, 0, Math.toRadians(-90));
        addManeuver(spacecraft, "V3", 277 * 60d, 1200, 1d, 0, Math.toRadians(0));
        addManeuver(spacecraft, "V4", 422 * 60d, 1400, 1d, 0, Math.toRadians(0));
        addManeuver(spacecraft, "V5", 881 * 60d, 1040, 1d, 0, Math.toRadians(0));
        addManeuver(spacecraft, "V6", 9319*60d, 525, 1d, 0, Math.toRadians(90));

        setTarget(spacecraft, venus);

        addMovingObject(spacecraft);

    }
}
