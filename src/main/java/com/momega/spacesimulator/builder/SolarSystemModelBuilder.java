package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.*;

/**
 * The builder of the solar system
 * Created by martin on 5/6/14.
 */
public class SolarSystemModelBuilder extends AbstractModelBuilder {

    @Override
    public void initPlanets() {
        Planet sun = new Planet();
        sun.setName("Sun");

        StaticTrajectory sunTrajectory = new StaticTrajectory();
        sunTrajectory.setPosition(new Vector3d(0, 0, 0));
        sunTrajectory.setTrajectoryColor(new double[]{0, 0.5, 1});
        sun.setTrajectory(sunTrajectory);
        updateDynamicalPoint(sun, "Sun", 1.989 * 1E6, 25.05, 696.342, 0);
        sun.setTextureFileName("sun.jpg");

        KeplerianTrajectory3d earthMoonTrajectory = createKeplerianTrajectory(sun, 149598.261d * 1E6, 0.0166739, 287.5824, 365.256814, 2456661.138788696378, 0.0018601064, 175.395d);
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
        updateDynamicalPoint(mercury, "Mercury", 0.3302, 58.646, 2.4397, 2.11d / 60d);
        mercury.setTrajectory(mercuryTrajectory);
        mercury.setTextureFileName("mercury.jpg");

        KeplerianTrajectory3d jupiterTrajectory = createKeplerianTrajectory(sun, 778547.2d * 1E6, 0.048775, 274.008653, 4332.59, 2455638.655976880342, 1.303541, 100.5118);
        jupiterTrajectory.setTrajectoryColor(new double[]{1,0.65,0.0});
        Planet jupiter = new Planet();
        updateDynamicalPoint(jupiter, "Jupiter", 1898.13, 9.925d / 24, 69.911, 3.13);
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


        Planet io = new Planet();
        io.setTrajectory(createKeplerianTrajectory(jupiter, 421.769 * 1E6, 0.0041, 136.11730, 1.769138, 2456821.035697427578, 0.036, 337.181));
        io.getTrajectory().setTrajectoryColor(new double[] {1,1,1});
        updateDynamicalPoint(io, "Io",  893.3E-4, 1.769138, 1.8213, 0d);
        io.setTextureFileName("io.jpg");
        addDynamicalPoint(io);

        Planet europa = new Planet();
        europa.setTrajectory(createKeplerianTrajectory(jupiter, 671.079 * 1E6, 0.0101, 302.75, 3.551810, 2456822.782242114656, 0.464, 343.685));
        europa.getTrajectory().setTrajectoryColor(new double[]{1, 1, 1});
        updateDynamicalPoint(europa, "Europa", 479.7E-4, 3.551810, 1.565, 0.1d);
        europa.setTextureFileName("europa.jpg");
        addDynamicalPoint(europa);

        Planet ganymede = new Planet();
        ganymede.setTrajectory(createKeplerianTrajectory(jupiter, 1070.0428 * 1E6, 0.0006, 342.14, 7.154553, 2456819.953914982267, 0.186, 340.928));
        ganymede.getTrajectory().setTrajectoryColor(new double[] {1,1,1});
        updateDynamicalPoint(ganymede, "Ganymede", 1482E-4, 7.154553, 2.634, 0.33d);
        ganymede.setTextureFileName("ganymede.jpg");
        addDynamicalPoint(ganymede);

        Planet callisto = new Planet();
        callisto.setTrajectory(createKeplerianTrajectory(jupiter, 1883 * 1E6, 0.007, 263.79, 16.689018, 2456815.215215998702, 0.281, 337.0061));
        callisto.getTrajectory().setTrajectoryColor(new double[] {1,1,1});
        updateDynamicalPoint(callisto, "Callisto", 1076E-4, 16.689018, 2.403, 0.22d);
        callisto.setTextureFileName("callisto.jpg");
        addDynamicalPoint(callisto);

        Planet phobos = new Planet();
        phobos.setTrajectory(createKeplerianTrajectory(mars, 9.3772 * 1E6, 0.0151, 121.451, 0.319, 2456821.639245583210, 1.082, 82.446));
        phobos.getTrajectory().setTrajectoryColor(new double[] {1,1,1});
        updateDynamicalPoint(phobos, "Phobos", 1.08E-8, 0.319, 13.1E-3, 0d);
        phobos.setTextureFileName("phobos.jpg");
        addDynamicalPoint(phobos);

        Planet deimos = new Planet();
        deimos.setTrajectory(createKeplerianTrajectory(mars, 23.4632 * 1E6, 0.00033, 306.201, 1.263, 2456821.036168867722, 1.791, 78.74157));
        deimos.getTrajectory().setTrajectoryColor(new double[] {1,1,1});
        updateDynamicalPoint(deimos, "Deimos", 1.80E-9, 1.263, 7.8E-3, 0d);
        deimos.setTextureFileName("deimos.jpg");
        addDynamicalPoint(deimos);

        SphereOfInfluence sunSoi = addPlanetToSoiTree(sun, null);
        addPlanetToSoiTree(mercury, sunSoi);
        addPlanetToSoiTree(venus, sunSoi);
        SphereOfInfluence earthSoi = addPlanetToSoiTree(earth, sunSoi, (KeplerianTrajectory2d) earthMoonBarycenter.getTrajectory());
        addPlanetToSoiTree(moon, earthSoi);
        SphereOfInfluence marsSoi = addPlanetToSoiTree(mars, sunSoi);
        addPlanetToSoiTree(phobos, marsSoi);
        addPlanetToSoiTree(deimos, marsSoi);
        SphereOfInfluence jupiterSoi = addPlanetToSoiTree(jupiter, sunSoi);
        addPlanetToSoiTree(io, jupiterSoi);
        addPlanetToSoiTree(europa, jupiterSoi);
        addPlanetToSoiTree(ganymede, jupiterSoi);
        addPlanetToSoiTree(callisto, jupiterSoi);

        model.setSelectedDynamicalPoint(earth);
    }

    @Override
    public void initSatellites() {
        Planet earth = (Planet) findDynamicalPoint("Earth");
        Planet moon = (Planet) findDynamicalPoint("Moon");

        Vector3d sv = earth.getPosition().normalize().scale(-9500d).add(earth.getVelocity());
        Satellite satellite = createSatellite(earth, "Satellite 4", 250, sv);
        addDynamicalPoint(satellite);

        sv = moon.getPosition().normalize().scale(1900d).add(moon.getVelocity());
        satellite = createSatellite(moon, "Satellite 6", 250, sv);
        addDynamicalPoint(satellite);
    }
}
