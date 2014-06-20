package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.*;

/**
 * Simple earth-moon model with the satellites
 * Created by martin on 5/6/14.
 *
 */
public class EarthSystemModelBuilder extends AbstractModelBuilder {

    @Override
    public void initPlanets() {
        Planet earth = new Planet();
        earth.setName("Earth");

        StaticTrajectory earthTrajectory = new StaticTrajectory();
        earthTrajectory.setPosition(new Vector3d(0,0,0));
        earthTrajectory.setTrajectoryColor(new double[]{0, 0.5, 1});
        earth.setTrajectory(earthTrajectory);
        updateDynamicalPoint(earth, "Earth", 5.97219, 0.997269, 6.378, 23.5);
        earth.setTextureFileName("earth.jpg");

        KeplerianTrajectory3d moonTrajectory = createKeplerianTrajectory(earth, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.241500, 208.1199);
        moonTrajectory.setTrajectoryColor(new double[] {0.5,0.5,0.5});
        Planet moon = new Planet();
        updateDynamicalPoint(moon, "Moon", 0.07349, 27.321, 1.737, 6.687);
        moon.setTrajectory(moonTrajectory);
        moon.setTextureFileName("moon.jpg");

        addDynamicalPoint(earth);
        addDynamicalPoint(moon);

        addPlanetToSoiTree(earth, null);
        addPlanetToSoiTree(moon, earth);

        model.setSelectedDynamicalPoint(earth);
    }

    @Override
    public void initSatellites() {
        Planet earth = (Planet) findDynamicalPoint("Earth");
        Planet moon = (Planet) findDynamicalPoint("Moon");

        Satellite satellite = createSatellite(earth, "Satellite 1", 200, earth.getOrientation().getU().scale(10000d));
        addDynamicalPoint(satellite);

        satellite = createSatellite(earth, "Satellite 2", 300, new Vector3d(0d, 0d, 9000d));
        addDynamicalPoint(satellite);

        Vector3d sv = moon.getPosition().normalize().scale(-1800d).add(moon.getVelocity());
        satellite = createSatellite(moon, "Satellite 3", 50, sv);
        addDynamicalPoint(satellite);

        sv = moon.getPosition().normalize().scale(1700d).add(moon.getVelocity());
        satellite = createSatellite(moon, "Satellite 5", 80, sv);
        addDynamicalPoint(satellite);
    }
}
