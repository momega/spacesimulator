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
import org.springframework.stereotype.Component;

/**
 * Created by martin on 10/19/14.
 */
@Component
public class MarsSpacecraftsModelBuilder extends MediumSolarSystemModelBuilder {

    protected void initTime() {
        model.setTime(TimeUtils.fromDateTime(new DateTime(2016, 2, 22, 10, 0, DateTimeZone.UTC)));
    }

    @Override
    public void initSpacecrafts() {
        CelestialBody earth = (CelestialBody) findMovingObject("Earth");
        CelestialBody mars = (CelestialBody) findMovingObject("Mars");

        Vector3d position = KeplerianOrbit.getCartesianPosition(500 * 1E3 + earth.getRadius(), 0, 0, Math.PI, 2d);
        Vector3d top = earth.getOrientation().getV();
        Vector3d velocity = position.normalize().cross(top).scale(8200d).negate();
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

        Spacecraft spacecraft = createSpacecraft(earth, "Spacecraft 2", position, velocity, 2, new double[]{1, 1, 0}, subsystems);

        addManeuver(spacecraft, "M5", 0 * 60d, 1100d, 1d, 0, Math.toRadians(-90));
        addManeuver(spacecraft, "M6", 82 * 60d, 1200d, 1d, 0, Math.toRadians(-90));
        addManeuver(spacecraft, "M7", 107.5 * 60d, 2654d, 1d, 0, Math.toRadians(0));
        addManeuver(spacecraft, "M8", 116107 * 60d, 261d, 1d, 0, Math.toRadians(90));
        addManeuver(spacecraft, "M9", 264824 * 60d, 4, 1d, 0, Math.toRadians(90));
        addManeuver(spacecraft, "M10", 308000 * 60d, 96, 1d, Math.toRadians(90), Math.toRadians(45));
        addManeuver(spacecraft, "M11", 308100 * 60d, 40, 1d, Math.toRadians(45), Math.toRadians(-30));
        addManeuver(spacecraft, "M12", 310495 * 60d, 500, 1d, Math.toRadians(180), Math.toRadians(0));

        setTarget(spacecraft, mars);

    }

    @Override
    public String getName() {
        return "Medium Solar System model, mission to Mars";
    }

}
