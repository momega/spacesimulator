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

    @Override
    public void initPlanets() {
        CelestialBody earth = new CelestialBody();
        earth.setName("Earth");

        earth.setPosition(new Vector3d(0,0,0));
        earth.setVelocity(new Vector3d(0, 0, 0));
        Trajectory earthTrajectory = new Trajectory();
        earth.setTrajectory(createTrajectory(new double[]{0, 0.5, 1}, TrajectorySolverType.STATIC));
        updateDynamicalPoint(earth, "Earth", 5.97219, 0.997269, 6.378, 23.5);
        earth.setTextureFileName("earth.jpg");

        CelestialBody moon = new CelestialBody();
        moon.setKeplerianElements(createKeplerianElements(earth, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.241500, 208.1199));
        updateDynamicalPoint(moon, "Moon", 0.07349, 27.321, 1.737, 6.687);
        moon.setTrajectory(createTrajectory(new double[] {0.5,0.5,0.5}, TrajectorySolverType.KEPLERIAN));
        moon.setTextureFileName("moon.jpg");

        addDynamicalPoint(earth);
        addDynamicalPoint(moon);

        SphereOfInfluence rootSoi = addPlanetToSoiTree(earth, null);
        addPlanetToSoiTree(moon, rootSoi);

        model.setSelectedDynamicalPoint(moon);
    }

    @Override
    public void initSatellites() {
        CelestialBody earth = (CelestialBody) findDynamicalPoint("Earth");
        CelestialBody moon = (CelestialBody) findDynamicalPoint("Moon");

//        Vector3d position = VectorUtils.fromSphericalCoordinates(200 * 1E3 + earth.getRadius(), Math.PI/2, 0);
//        Vector3d velocity = new Vector3d(0, 10000d, 0);
//        Satellite satellite = createSatellite(earth, "Satellite 1", position, velocity);
//        addDynamicalPoint(satellite);

//        position = VectorUtils.fromSphericalCoordinates(50 * 1E3 + moon.getRadius(), Math.PI/2, 0);
//        velocity = new Vector3d(0, 2350d, 0);
//        satellite = createSatellite(moon, "Satellite 3", position, velocity);
//        addDynamicalPoint(satellite);
//
        Vector3d position = VectorUtils.fromSphericalCoordinates(2000 * 1E3 + moon.getRadius(), Math.PI/2, -Math.PI / 4);
        Vector3d velocity = new Vector3d(0, 1950d, 0);
        Satellite satellite = createSatellite(moon, "Satellite 4", position, velocity);
        addDynamicalPoint(satellite);

//        sv = moon.getPosition().normalize().scale(1700d).add(moon.getVelocity());
//        satellite = createSatellite(moon, "Satellite 5", 80, sv);
//        addDynamicalPoint(satellite);
    }

    protected void initCamera() {
        Camera s = new Camera();
        s.setDynamicalPoint(findDynamicalPoint("Moon"));
        s.setDistance(100 * 1E6);
        s.setPosition(new Vector3d(s.getDistance(), 0, 0));
        s.setOrientation(MathUtils.createOrientation(new Vector3d(-1, 0, 0), new Vector3d(0, 0, 1)));
        s.setOppositeOrientation(MathUtils.createOrientation(new Vector3d(1, 0, 0), new Vector3d(0, 0, 1)));
        model.setCamera(s);
    }
}
