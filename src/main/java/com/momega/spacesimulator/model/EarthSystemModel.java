package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;

import java.math.BigDecimal;
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
        universeService.updateDynamicalPoint(earth, "Earth", 5.97219, 0.997269, 6.378, 23.5);
        earth.setTextureFileName("earth.jpg");

        KeplerianTrajectory3d moonTrajectory = universeService.createKeplerianTrajectory(earth, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.241500, 208.1199);
        moonTrajectory.setTrajectoryColor(new double[] {0.5,0.5,0.5});
        Planet moon = new Planet();
        universeService.updateDynamicalPoint(moon, "Moon", 0.07349, 27.321, 1.737, 6.687);
        moon.setTrajectory(moonTrajectory);
        moon.setTextureFileName("moon.jpg");

        universeService.addDynamicalPoint(earth);
        universeService.addDynamicalPoint(moon);

        for(DynamicalPoint dp : universeService.getDynamicalPoints()) {
            dp.setTimestamp(getTime());
        }

        next();

        Satellite satellite = universeService.createSatellite(earth, "Satellite 1", 200, new Vector3d(0d, 10000d, 0d));
        universeService.addDynamicalPoint(satellite);

        satellite = universeService.createSatellite(earth, "Satellite 2", 300, new Vector3d(0d, 0d, 9000d));
        universeService.addDynamicalPoint(satellite);

//        satellite = universeService.createSatellite(moon, "Satellite 3", 50, Vector3d.scaleAdd(1, moon.getVelocity(), new Vector3d(0d, 1800d, 0d)));
//        universeService.addDynamicalPoint(satellite);

        for(DynamicalPoint dp : universeService.getSatellites()) {
            dp.setTimestamp(getTime());
        }

        setSelectedDynamicalPoint(earth);
    }

    @Override
    protected void initTime() {
        setTime(TimeUtils.createTime(2456820d));
        setWarpFactor(BigDecimal.ONE);
    }

    @Override
    protected void initCamera() {
        AttachedCamera s = new AttachedCamera();
        s.setDynamicalPoint(universeService.getSatellites().get(0));
        s.setDistance(100);
        s.setOrientation(MathUtils.createOrientation(new Vector3d(-1, 0, 0), new Vector3d(0, 0, 1)));

        FreeCamera f = new FreeCamera();
        f.setPosition(new Vector3d(15 * 1E6, 0, 0));
        f.setOrientation(MathUtils.createOrientation(new Vector3d(-1, 0, 0), new Vector3d(0, 0, 1)));
        f.setVelocity(1 * 1E6);
        setCamera(f);

        CompositeCamera c = new CompositeCamera();
        c.setCameras(Arrays.asList(s, f));
        c.updateCurrent(0);
        setCamera(c);
    }


}
