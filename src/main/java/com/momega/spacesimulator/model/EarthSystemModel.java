package com.momega.spacesimulator.model;

import java.util.Arrays;

/**
 * Created by martin on 5/6/14.
 */
public class EarthSystemModel extends AbstractModel {

    @Override
    public void initDynamicalPoints() {
        Planet earth = new Planet();
        earth.setName("Earth");

        StaticTrajectory earthTrajectory = new StaticTrajectory();
        earthTrajectory.setPosition(new Vector3d(0,0,0));
        earthTrajectory.setTrajectoryColor(new double[]{0, 0.5, 1});
        earth.setTrajectory(earthTrajectory);
        earth.setMass(5.97219 * 1E24);
        earth.setRotationPeriod(0.997269 * 86400);
        earth.setTextureFileName("earth.jpg");
        earth.setRadius(6.378 * 1E6);
        earth.setOrientation(new Orientation(new Vector3d(1, 0, 0), new Vector3d(0,0, 1)));
        earth.setAxialTitle(Math.toRadians(23.5));

        KeplerianTrajectory3d moonTrajectory = createKeplerianTrajectory(earth, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.241500, 208.1199);
        moonTrajectory.setTrajectoryColor(new double[] {0.5,0.5,0.5});
        Planet moon = new Planet();
        moon.setName("Moon");
        moon.setTrajectory(moonTrajectory);
        moon.setRadius(1.737 * 1E6);
        moon.setMass(0.07349 * 1E24);
        moon.setTextureFileName("moon.jpg");
        moon.setRotationPeriod(27.321 * 86400);
        moon.setOrientation(new Orientation(new Vector3d(1, 0, 0), new Vector3d(0,0, 1)));
        moon.setAxialTitle(Math.toRadians(6.687));

        addDynamicalPoint(earth);
        addDynamicalPoint(moon);


        Satellite satellite = new Satellite();
        satellite.setName("Satellite");
        satellite.setPosition(new Vector3d((6.378 + 0.2) * 1E6, 0, 0));
        satellite.setOrientation(new Orientation(new Vector3d(0, 1, 0d), new Vector3d(0, 0, 1d)));
        satellite.setVelocity(new Vector3d(0, 9000d, 0));
        NewtonianTrajectory satelliteTrajectory = new NewtonianTrajectory();
        satelliteTrajectory.setTrajectoryColor(new double[] {1,1,1});
        satelliteTrajectory.setPlanets(Arrays.asList(earth, moon));
        satellite.setTrajectory(satelliteTrajectory);
        satellite.setMass(10 * 1E3);

        addDynamicalPoint(satellite);
    }

    @Override
    protected void initCamera() {
        Camera c = new Camera();
        c.setPosition(new Vector3d(15 * 1E6, 0, 0));
        c.setOrientation(new Orientation(new Vector3d(-1, 0, 0), new Vector3d(0, 0, 1)));
        c.setVelocity(1*1E6);
        setCamera(c);
    }


}
