package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.VectorUtils;

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
        setCentralPoint(earth);
        createTrajectory(earth, new double[]{0, 0.5, 1}, TrajectoryType.STATIC);
        updateDynamicalPoint(earth, "Earth", 5.97219, 0.997269, 6.378, 23.5, null);
        earth.setTextureFileName("earth.jpg");

        CelestialBody moon = new CelestialBody();
        createKeplerianElements(moon, earth, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.145, 208.1199);
        updateDynamicalPoint(moon, "Moon", 0.07349, 27.321, 1.737, 269.9949, 66.5392, null);
        createTrajectory(moon, new double[] {0.5,0.5,0.5}, TrajectoryType.KEPLERIAN);
        moon.setTextureFileName("moon.jpg");

        addDynamicalPoint(earth);
        addDynamicalPoint(moon);

        SphereOfInfluence earthSoi = addPlanetToSoiTree(earth, null);
        addPlanetToSoiTree(moon, earthSoi);

        model.setSelectedDynamicalPoint(earth);
    }

    @Override
    public void initSatellites() {
        CelestialBody earth = (CelestialBody) findDynamicalPoint("Earth");
        CelestialBody moon = (CelestialBody) findDynamicalPoint("Moon");

        Vector3d position = VectorUtils.fromSphericalCoordinates(200 * 1E3 + earth.getRadius(), Math.PI/2, 0);
        Orientation o = MathUtils.createOrientation(new Vector3d(0, 1d, 0), new Vector3d(0, 0, 1d));
        o.lookUp(Math.toRadians(-23.75d));
        Vector3d velocity = o.getN().scale(9000d);
        Spacecraft spacecraft = createSatellite(earth, "Spacecraft 1", position, velocity);
        addDynamicalPoint(spacecraft);

//        Vector3d position = VectorUtils.fromSphericalCoordinates(200 * 1E3 + earth.getRadius(), Math.PI/2, 0);
//        Vector3d velocity = new Vector3d(0, 10000d, 0);
//        Spacecraft spacecraft = createSatellite(earth, "Spacecraft 1", position, velocity);
//        addDynamicalPoint(spacecraft);

//        position = VectorUtils.fromSphericalCoordinates(50 * 1E3 + moon.getRadius(), Math.PI/2, 0);
//        velocity = new Vector3d(0, 2350d, 0);
//        spacecraft = createSatellite(moon, "Spacecraft 3", position, velocity);
//        addDynamicalPoint(spacecraft);

//        Vector3d position = VectorUtils.fromSphericalCoordinates(2000 * 1E3 + moon.getRadius(), Math.PI/2, -Math.PI / 4);
//        Vector3d velocity = new Vector3d(0, 1950d, 0);
//        Spacecraft spacecraft = createSatellite(moon, "Spacecraft 4", position, velocity);
//        addDynamicalPoint(spacecraft);

//        sv = moon.getPosition().normalize().scale(1700d).add(moon.getVelocity());
//        spacecraft = createSatellite(moon, "Spacecraft 5", 80, sv);
//        addDynamicalPoint(spacecraft);
    }

    @Override
    protected MovingObject getCentralObject() {
        return earth;
    }

    protected void initCamera() {
        Camera s = new Camera();
        s.setTargetObject(findDynamicalPoint("Earth"));
        s.setDistance(100 * 1E6);
        s.setOppositeOrientation(MathUtils.createOrientation(new Vector3d(1, 0, 0), new Vector3d(0, 0, 1)));
        model.setCamera(s);
    }
}
