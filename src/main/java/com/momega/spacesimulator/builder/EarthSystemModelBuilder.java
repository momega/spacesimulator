package com.momega.spacesimulator.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.momega.spacesimulator.model.*;

/**
 * The builder or simple earth-moon model with the satellites
 * Created by martin on 5/6/14.
 *
 */
public class EarthSystemModelBuilder extends AbstractModelBuilder {

    private CelestialBody earth;

    @Override
    public void initPlanets() {
        earth = new Planet();
        earth.setName("Earth");
        updateDynamicalPoint(earth, "Earth", 5.97219, 0.997269, 6.378, 23.5, "Earth", "/images/earth.png");
        createTrajectory(earth, new double[]{0, 0.5, 1});
        earth.setTextureFileName("earth_hi.jpg");
        setCentralPoint(earth);

        CelestialBody moon = new CelestialBody();
        updateDynamicalPoint(moon, "Moon", 0.07349, 27.321, 1.737, 269.9949, 66.5392, "Moon", "/images/moon.png");
        createKeplerianElements(moon, earth, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.145, 208.1199);
        createTrajectory(moon, new double[] {0.5,0.5,0.5});
        moon.setTextureFileName("moon_c.jpg");

        addMovingObject(earth);
        addMovingObject(moon);

        SphereOfInfluence earthSoi = addPlanetToSoiTree(earth, null);
        addPlanetToSoiTree(moon, earthSoi);
    }

    @Override
    public void initSpacecrafts() {
        CelestialBody earth = (CelestialBody) findMovingObject("Earth");
        CelestialBody moon = (CelestialBody) findMovingObject("Moon");
        
        KeplerianOrbit keplerianOrbit = new KeplerianOrbit();
        keplerianOrbit.setArgumentOfPeriapsis(0);
        keplerianOrbit.setAscendingNode(0);
        keplerianOrbit.setCentralObject(earth);
        keplerianOrbit.setEccentricity(0.001);
        keplerianOrbit.setInclination(0);
        keplerianOrbit.setPeriod(BigDecimal.valueOf(90.0 * 60));
        keplerianOrbit.setSemimajorAxis(250 * 1E3 + earth.getRadius());
        keplerianOrbit.setTimeOfPeriapsis(getTime());
        
        KeplerianElements ke = new KeplerianElements();
        ke.setKeplerianOrbit(keplerianOrbit);
        ke.setTrueAnomaly(180.0);
        
        CartesianState cartesianState = ke.toCartesianState();

        Vector3d position = cartesianState.getPosition();
        Vector3d velocity = cartesianState.getVelocity();
        
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
        
        Spacecraft spacecraft = createSpacecraft(earth, "Spacecraft 1", position, velocity, 1, new double[] {1, 1, 0}, subsystems);

        setTarget(spacecraft, moon);
    }

    @Override
    protected MovingObject getCentralObject() {
        return earth;
    }

    protected void initCamera() {
        createCamera(findMovingObject("Earth"));
    }
}
