package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.BaryCentre;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.HabitableModule;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.Propulsion;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.SphereOfInfluence;
import com.momega.spacesimulator.model.TrajectoryType;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.utils.KeplerianUtils;

/**
 * The builder creates very simple model of the solar system just with the sun, moon and earth.
 * The moon and earth are orbiting common barycentre.
 * Created by martin on 7/14/14.
 */
public class SimpleSolarSystemModelBuilder extends AbstractModelBuilder {

    protected CelestialBody sun;
    protected SphereOfInfluence sunSoi;

    @Override
    public void initPlanets() {
        sun = new CelestialBody();
        updateDynamicalPoint(sun, "Sun", 1.989 * 1E6, 25.05, 696.342, 286.13, 63.87, "Sun");
        createTrajectory(sun, new double[] {1, 0.7, 0}, TrajectoryType.STATIC);
        sun.setTextureFileName("sun.jpg");
        setCentralPoint(sun);

        BaryCentre earthMoonBarycenter = new BaryCentre();
        createKeplerianElements(earthMoonBarycenter, sun, 149598.261d * 1E6, 0.0166739, 287.5824, 365.256814, 2456661.138788696378, 0.0018601064, 175.395d);
        updateDynamicalPoint(earthMoonBarycenter, "Earth-Moon Barycenter", 0, 0, 1, 0, null);
        createTrajectory(earthMoonBarycenter, new double[]{0, 0.5, 1}, TrajectoryType.KEPLERIAN);

        CelestialBody earth = new Planet();
        createKeplerianElements(earth, earthMoonBarycenter, 4.686955382086 * 1E6, 0.055557, 264.7609, 27.427302, 2456796.39770, 5.241500, 208.1199);
        updateDynamicalPoint(earth, "Earth", 5.97219, 0.997269, 6.371, 0d, 90d, 190.147d,  "Earth");
        createTrajectory(earth, new double[]{0, 0.5, 1}, TrajectoryType.KEPLERIAN);
        earth.setTextureFileName("earth.jpg");

        CelestialBody moon = new Planet();
        createKeplerianElements(moon, earthMoonBarycenter, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.241500, 208.1199);
        updateDynamicalPoint(moon, "Moon", 0.07349, 27.321, 1.737, 6.687, "Moon");
        createTrajectory(moon, new double[]{0.5,0.5,0.5}, TrajectoryType.KEPLERIAN);
        moon.setTextureFileName("moon.jpg");

        addMovingObject(sun);
        addMovingObject(earthMoonBarycenter);
        addMovingObject(earth);
        addMovingObject(moon);

        sunSoi = addPlanetToSoiTree(sun, null);
        SphereOfInfluence earthSoi = addPlanetToSoiTree(earth, sunSoi, earthMoonBarycenter.getKeplerianElements());
        addPlanetToSoiTree(moon, earthSoi);

        model.setSelectedObject(earth);
    }

    @Override
    public void initSpacecrafts() {
        CelestialBody earth = (CelestialBody) findMovingObject("Earth");
        CelestialBody moon = (CelestialBody) findMovingObject("Moon");

        Vector3d position = KeplerianUtils.getInstance().getCartesianPosition(200 * 1E3 + earth.getRadius(), Math.PI / 2, Math.toRadians(23.439291), Math.PI, 2d);
        Vector3d top = earth.getOrientation().getV();
        Vector3d velocity = position.normalize().cross(top).scale(8200d).negate();
        Spacecraft spacecraft = createSpacecraft(earth, "Spacecraft 1", position, velocity);

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

        addManeuver(spacecraft, "M1", 20 * 60d, 2600d, 1d, 0, Math.toRadians(90));
        addManeuver(spacecraft, "M2", 130 * 60d, 950d, 1d, 0, Math.toRadians(90));
        addManeuver(spacecraft, "M3", 200 * 60d, 1500d, 1d, 0, Math.toRadians(0));
        addManeuver(spacecraft, "M4", 5915 * 60d, 210d, 1d, Math.toRadians(180), Math.toRadians(0));
        
        spacecraft.setTargetBody(moon);

        addMovingObject(spacecraft);
    }

    @Override
    protected MovingObject getCentralObject() {
        return sun;
    }
}
