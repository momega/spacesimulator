package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.TimeUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 1/11/15.
 */
@Component
public class CrashingSpacecraftModelBuilder extends SimpleSolarSystemModelBuilder {

    protected void initTime() {
        model.setTime(TimeUtils.fromDateTime(new DateTime(2015, 1, 9, 12, 0, DateTimeZone.UTC)));
    }

    @Override
    public void initSpacecrafts() {
        CelestialBody earth = (CelestialBody) findMovingObject("Earth");

        Vector3d position = KeplerianOrbit.getCartesianPosition(300 * 1E3 + earth.getRadius(), Math.PI / 2, Math.toRadians(23.439291), Math.PI, 1.2d);
        Vector3d top = earth.getOrientation().getV();
        Vector3d velocity = position.normalize().cross(top).scale(7000d).negate().add(new Vector3d(0, 0, 5000));

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

        createSpacecraft(earth, "Spacecraft 5", position, velocity, 5, new double[] {1, 1, 0}, subsystems);
    }

    @Override
    public String getName() {
        return "Crashing Spacecraft model";
    }
}
