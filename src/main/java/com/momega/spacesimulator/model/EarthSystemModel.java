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
        earth.setMass(5.97219 * 1E24);
        earth.setRotationPeriod(0.997269 * 86400);
        earth.setTextureFileName("earth.jpg");
        earth.setRadius(6.378 * 1E6);
        earth.setOrientation(new Orientation(new Vector3d(1, 0, 0), new Vector3d(0,0, 1)));
        earth.setAxialTilt(Math.toRadians(23.5));
        earth.setTimestamp(getTime().getTimestamp());

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
        moon.setAxialTilt(Math.toRadians(6.687));
        moon.setTimestamp(getTime().getTimestamp());

        addDynamicalPoint(earth);
        addDynamicalPoint(moon);

        Satellite satellite = new Satellite();
        satellite.setName("Satellite");
        satellite.setPosition(new Vector3d((6.378 + 0.2) * 1E6, 0, 0));
        satellite.setOrientation(new Orientation(new Vector3d(0, 1, 0d), new Vector3d(0, 0, 1d)));
        satellite.setVelocity(new Vector3d(0, 9500d, 0));
        NewtonianTrajectory satelliteTrajectory = new NewtonianTrajectory();
        satelliteTrajectory.setTrajectoryColor(new double[] {1,1,1});
        satellite.setTrajectory(satelliteTrajectory);
        satellite.setMass(10 * 1E3);
        satellite.setRadius(10);
        satellite.setTimestamp(getTime().getTimestamp());

        addDynamicalPoint(satellite);
    }

    @Override
    protected void initTime() {
        setTime(new Time(2456760d, 1d));
    }

    @Override
    protected void initCamera() {
        SatelliteCamera s = new SatelliteCamera();
        s.setSatellite(getSatellites().get(0));
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
