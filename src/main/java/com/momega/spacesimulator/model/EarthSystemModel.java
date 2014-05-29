package com.momega.spacesimulator.model;

import java.util.Arrays;

/**
 * Simple earth-moon model with the satellite
 * Created by martin on 5/6/14.
 *
 * //TODO: remove this method to the service package
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

        Satellite satellite = new Satellite();
        satellite.setName("Satellite");
        satellite.setPosition(new Vector3d((6.378 + 0.2) * 1E6, 0, 0));
        satellite.setOrientation(new Orientation(new Vector3d(0, 1, 0d), new Vector3d(0, 0, 1d)));
        satellite.setVelocity(new Vector3d(0, 9500d, 0));
        NewtonianTrajectory satelliteTrajectory = new NewtonianTrajectory();
        satelliteTrajectory.setTrajectoryColor(new double[]{1, 1, 1});
        satellite.setTrajectory(satelliteTrajectory);
        satellite.setMass(10 * 1E3);
        satellite.setRadius(10);

        addDynamicalPoint(satellite);

        for(DynamicalPoint dp : getDynamicalPoints()) {
            dp.setTimestamp(getTime().getTimestamp());
        }
    }

    @Override
    protected void initTime() {
        setTime(new Time(2456790d, 1d));
    }

    @Override
    protected void initCamera() {
        AttachedCamera s = new AttachedCamera();
        s.setDynamicalPoint(getSatellites().get(0));
        s.setDistance(100);
        s.setOrientation(new Orientation(new Vector3d(-1, 0, 0), new Vector3d(0, 0, 1)));

        FreeCamera f = new FreeCamera();
        f.setPosition(new Vector3d(15 * 1E6, 0, 0));
        f.setOrientation(new Orientation(new Vector3d(-1, 0, 0), new Vector3d(0, 0, 1)));
        f.setVelocity(1 * 1E6);
        setCamera(f);

        CompositeCamera c = new CompositeCamera();
        c.setCameras(Arrays.asList(s, f));
        c.updateCurrent(0);
        setCamera(c);
    }


}
