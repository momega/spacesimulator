package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.*;

/**
 * Created by martin on 10/19/14.
 */
public class TwoSpacecraftsModelBuilder extends MediumSolarSystemModelBuilder {

    @Override
    public void initSpacecrafts() {
        super.initSpacecrafts();

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
        addManeuver(spacecraft, "M6", 82 * 60d, 1400d, 1d, 0, Math.toRadians(-90));

        setTarget(spacecraft, mars);

        addMovingObject(spacecraft);

    }
}
