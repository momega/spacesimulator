package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.utils.TimeUtils;
import com.momega.spacesimulator.utils.VectorUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;
import com.momega.spacesimulator.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The builder for the second mission to moon.
 * Created by martin on 1/9/15.
 */
@Component
public class MoonMission2ModelBuilder extends SimpleSolarSystemModelBuilder {

    @Override
    public void initSpacecrafts() {
        CelestialBody earth = (CelestialBody) findMovingObject("Earth");
        CelestialBody moon = (CelestialBody) findMovingObject("Moon");

        Vector3d position = KeplerianOrbit.getCartesianPosition(300 * 1E3 + earth.getRadius(), Math.PI / 2, Math.toRadians(5), Math.PI, 0d);
        Vector3d top = earth.getOrientation().getV();
        Vector3d velocity = position.normalize().cross(top).negate();
        velocity = VectorUtils.rotateAboutAxis(velocity, Math.toRadians(-2.7), earth.getOrientation().getU());
        velocity = velocity.scale(8400);

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

        Spacecraft spacecraft = createSpacecraft(earth, "Spacecraft 4", position, velocity, 4, new double[] {1, 1, 0}, subsystems);

        addManeuver(spacecraft, "A1", 27 * 60d, 94d, 1d, 0, Math.toRadians(-90));
        addManeuver(spacecraft, "A2", 110 * 60d, 1500d, 1d, 0, Math.toRadians(0));
        addManeuver(spacecraft, "A3", 262 * 60d + 20, 2403d, 1d, 0, Math.toRadians(0));
        addManeuver(spacecraft, "A4", 4395 * 60d + 35, 19d, 1d, Math.toRadians(90), Math.toRadians(0));
        addManeuver(spacecraft, "A5", 5064 * 60d, 470, 1d, Math.toRadians(180), Math.toRadians(0));
        addManeuver(spacecraft, "A6", 5145 * 60d + 20, 350, 1d, Math.toRadians(0), Math.toRadians(0));
        addManeuver(spacecraft, "A7", 11290 * 60d, 600, 1d, Math.toRadians(180), Math.toRadians(0));

        setTarget(spacecraft, moon);

        CrashSite crashSite = CrashSite.createFromLatLong(earth, model.getTime(), 14.5, 50);
        crashSite.setName("Crash Site");
        earth.getSurfacePoints().add(crashSite);
    }

    protected void initTime() {
        model.setTime(TimeUtils.fromDateTime(new DateTime(2015, 1, 9, 12, 0, DateTimeZone.UTC)));
    }

    @Override
    public String getName() {
        return "Simple Solar System model, mission 2 to Moon";
    }
}

