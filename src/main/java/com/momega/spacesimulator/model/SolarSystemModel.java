package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.TimeUtils;

import java.util.Arrays;

/**
 * Created by martin on 5/6/14.
 */
public class SolarSystemModel extends AbstractModel {

    @Override
    public void initDynamicalPoints() {
        Planet sun = new Planet();
        sun.setName("Sun");

        StaticTrajectory sunTrajectory = new StaticTrajectory();
        sunTrajectory.setPosition(new Vector3d(0, 0, 0));
        sunTrajectory.setTrajectoryColor(new double[]{0, 0.5, 1});
        sun.setTrajectory(sunTrajectory);
        updateDynamicalPoint(sun, "Sun", 1.989 * 1E6, 25.05, 696.342, 0);
        sun.setTextureFileName("sun.jpg");

        KeplerianTrajectory3d earthMoonTrajectory = createKeplerianTrajectory(sun, 149598.261d *  1E6, 0.0166739, 287.5824, 365.256814, 2456661.138788696378, 0.0018601064, 175.395d);
        earthMoonTrajectory.setTrajectoryColor(new double[] {0,0.5,1});
        DynamicalPoint earthMoonBarycenter = new DynamicalPoint();
        updateDynamicalPoint(earthMoonBarycenter, "Earth-Moon Barycenter", 0, 0, 1, 6.687);
        earthMoonBarycenter.setTrajectory(earthMoonTrajectory);

        KeplerianTrajectory3d earthTrajectory = createKeplerianTrajectory(earthMoonBarycenter, 4.686955382086d, 0.055557, 264.7609, 27.427302, 2456796.39770, 5.241500, 208.1199);
        earthTrajectory.setTrajectoryColor(new double[]{0, 0.5, 1});
        Planet earth = new Planet();
        updateDynamicalPoint(earth, "Earth", 5.97219, 0.997269, 6.378, 23.5);
        earth.setTrajectory(earthTrajectory);
        earth.setTextureFileName("earth.jpg");

        KeplerianTrajectory3d moonTrajectory = createKeplerianTrajectory(earthMoonBarycenter, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.241500, 208.1199);
        moonTrajectory.setTrajectoryColor(new double[] {0.5,0.5,0.5});
        Planet moon = new Planet();
        updateDynamicalPoint(moon, "Moon", 0.07349, 27.321, 1.737, 6.687);
        moon.setTrajectory(moonTrajectory);
        moon.setTextureFileName("moon.jpg");

        KeplerianTrajectory3d marsTrajectory = createKeplerianTrajectory(sun, 227939.1d * 1E6, 0.093315, 286.537, 686.9363, 2457003.918154194020, 1.84844, 49.5147);
        marsTrajectory.setTrajectoryColor(new double[] {1,0,0});
        Planet mars = new Planet();
        updateDynamicalPoint(mars, "Mars", 0.64185, 1.02595, 3.3895, 25.19);
        mars.setTrajectory(marsTrajectory);
        mars.setTextureFileName("mars.jpg");

        KeplerianTrajectory3d venusTrajectory = createKeplerianTrajectory(sun, 108208d * 1E6, 0.0067, 54.6820, 224.699, 2456681.501144, 3.3945, 76.6408);
        venusTrajectory.setTrajectoryColor(new double[] {1,1,0});
        Planet venus = new Planet();
        updateDynamicalPoint(venus, "Venus", 4.8685, 243.0185, 6.0518, 177.36);
        venus.setTrajectory(venusTrajectory);
        venus.setTextureFileName("venus.jpg");

        KeplerianTrajectory3d mercuryTrajectory = createKeplerianTrajectory(sun, 57909.05d * 1E6, 0.20563, 29.124, 87.96890, 2456780.448693044949, 7.0, 48.313);
        mercuryTrajectory.setTrajectoryColor(new double[]{0.2,0.2,0.2});
        Planet mercury = new Planet();
        updateDynamicalPoint(mercury, "Mercury", 0.3302, 58.646, 2.4397, 2.11d/60d);
        mercury.setTrajectory(mercuryTrajectory);
        mercury.setTextureFileName("mercury.jpg");

        KeplerianTrajectory3d jupiterTrajectory = createKeplerianTrajectory(sun, 778547.2d * 1E6, 0.048775, 274.008653, 4332.59, 2455638.655976880342, 1.303541, 100.5118);
        jupiterTrajectory.setTrajectoryColor(new double[]{1,0.65,0.0});
        Planet jupiter = new Planet();
        updateDynamicalPoint(jupiter, "Jupiter", 1898.13, 9.925d/24, 69.911, 3.13);
        jupiter.setTrajectory(jupiterTrajectory);
        jupiter.setTextureFileName("jupiter.jpg");

        addDynamicalPoint(sun);
        addDynamicalPoint(earthMoonBarycenter);
        addDynamicalPoint(earth);
        addDynamicalPoint(moon);
        addDynamicalPoint(mars);
        addDynamicalPoint(venus);
        addDynamicalPoint(mercury);
        addDynamicalPoint(jupiter);

        for(DynamicalPoint dp : getDynamicalPoints()) {
            dp.setTimestamp(getTime().getTimestamp());
        }

        next();
    }

    @Override
    protected void initTime() {
        setTime(TimeUtils.createTime(2456820d, 1d));
    }

    @Override
    protected void initCamera() {
        AttachedCamera s = new AttachedCamera();
        s.setDynamicalPoint(findDynamicalPoint("Earth"));
        s.setDistance(15 * 1E6);
        s.setOrientation(createOrientation(new Vector3d(-1, 0, 0), new Vector3d(0, 0, 1)));

        FreeCamera f = new FreeCamera();
        f.setPosition(new Vector3d(15 * 1E9, 0, 0));
        f.setOrientation(createOrientation(new Vector3d(-1, 0, 0), new Vector3d(0, 0, 1)));
        f.setVelocity(1 * 1E6);
        setCamera(f);

        CompositeCamera c = new CompositeCamera();
        c.setCameras(Arrays.asList(f, s));
        c.updateCurrent(0);
        setCamera(c);
    }
}
