package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.SphereOfInfluence;
import com.momega.spacesimulator.model.TrajectoryType;

/**
 * The builder of the solar system
 * Created by martin on 5/6/14.
 */
public class SolarSystemModelBuilder extends SimpleSolarSystemModelBuilder {

    @Override
    public void initPlanets() {
        super.initPlanets();

        CelestialBody mars = new Planet();
        createKeplerianElements(mars, sun, 227939.1d * 1E6, 0.093315, 286.537, 686.9363, 2457003.918154194020, 1.84844, 49.5147);
        updateDynamicalPoint(mars, "Mars", 0.64185, 1.02595, 3.3895, 317.68143, 52.88650, 176.630, "Mars_(planet)");
        createTrajectory(mars, new double[]{1, 0, 0}, TrajectoryType.KEPLERIAN);
        mars.setTextureFileName("mars.jpg");

        CelestialBody venus = new Planet();
        createKeplerianElements(venus, sun, 108208d * 1E6, 0.0067, 54.6820, 224.699, 2456681.501144, 3.3945, 76.6408);
        updateDynamicalPoint(venus, "Venus", 4.8685, 243.0185, 6.0518, 272.76, 67.16, 160.20, "Venus_(planet)");
        createTrajectory(venus, "#FF9933", TrajectoryType.KEPLERIAN);
        venus.setTextureFileName("venus.jpg");

        CelestialBody mercury = new Planet();
        createKeplerianElements(mercury, sun, 57909.05d * 1E6, 0.20563, 29.124, 87.96890, 2456780.448693044949, 7.0, 48.313);
        updateDynamicalPoint(mercury, "Mercury", 0.3302, 58.646, 2.4397, 281.01, 61.45, 329.5469, "Mercury_(planet)");
        createTrajectory(mercury, new double[]{0.2, 0.2, 0.2}, TrajectoryType.KEPLERIAN);
        mercury.setTextureFileName("mercury.jpg");

        CelestialBody jupiter = new Planet();
        createKeplerianElements(jupiter, sun, 778547.2d * 1E6, 0.048775, 274.008653, 4332.59, 2455638.655976880342, 1.303541, 100.5118);
        updateDynamicalPoint(jupiter, "Jupiter", 1898.13, 9.925d / 24, 69.911, 268.05, 64.49, 284.95, "Jupiter_(planet)");
        createTrajectory(jupiter, new double[]{1,0.65,0.0}, TrajectoryType.KEPLERIAN);
        jupiter.setTextureFileName("jupiter.jpg");

        addMovingObject(mars);
        addMovingObject(venus);
        addMovingObject(mercury);
        addMovingObject(jupiter);

        CelestialBody io = new CelestialBody();
        createKeplerianElements(io, jupiter, 421.769 * 1E6, 0.0041, 136.11730, 1.769138, 2456821.035697427578, 2.24147, 337.181);
        createTrajectory(io, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(io, "Io",  893.3E-4, 1.769138, 1.8213, 0d, "Io_(moon)");
        io.setTextureFileName("io.jpg");
        addMovingObject(io);

        CelestialBody europa = new CelestialBody();
        createKeplerianElements(europa, jupiter, 671.079 * 1E6, 0.0101, 302.75, 3.551810, 2456822.782242114656, 2.62907342, 343.685);
        createTrajectory(europa, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(europa, "Europa", 479.7E-4, 3.551810, 1.565, 0.1d, "Europa_(moon)");
        europa.setTextureFileName("europa.jpg");
        addMovingObject(europa);

        CelestialBody ganymede = new CelestialBody();
        createKeplerianElements(ganymede, jupiter, 1070.0428 * 1E6, 0.0006, 342.14, 7.154553, 2456819.953914982267, 2.2829570, 340.928);
        createTrajectory(ganymede, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(ganymede, "Ganymede", 1482E-4, 7.154553, 2.634, 0.33d, null);
        ganymede.setTextureFileName("ganymede.jpg");
        addMovingObject(ganymede);

        CelestialBody callisto = new CelestialBody();
        createKeplerianElements(callisto, jupiter, 1883 * 1E6, 0.007, 263.79, 16.689018, 2456815.215215998702, 1.9812553570, 337.0061);
        createTrajectory(callisto, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(callisto, "Callisto", 1076E-4, 16.689018, 2.403, 0.22d, null);
        callisto.setTextureFileName("callisto.jpg");
        addMovingObject(callisto);

        CelestialBody phobos = new CelestialBody();
        createKeplerianElements(phobos, mars, 9.3772 * 1E6, 0.0151, 121.451, 0.319, 2456821.639245583210, 27.7682593856, 82.446);
        createTrajectory(phobos, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(phobos, "Phobos", 1.08E-8, 0.319, 13.1E-3, 0d, "Phobos_(moon)");
        phobos.setTextureFileName("phobos.jpg");
        addMovingObject(phobos);

        CelestialBody deimos = new CelestialBody();
        createKeplerianElements(deimos, mars, 23.4632 * 1E6, 0.00033, 306.201, 1.263, 2456821.036168867722, 26.1262612, 78.74157);
        createTrajectory(deimos, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(deimos, "Deimos", 1.80E-9, 1.263, 7.8E-3, 0d, "Deimos_(moon)");
        deimos.setTextureFileName("deimos.jpg");
        addMovingObject(deimos);

        SphereOfInfluence marsSoi = addPlanetToSoiTree(mars, sunSoi);
        addPlanetToSoiTree(phobos, marsSoi);
        addPlanetToSoiTree(deimos, marsSoi);
        SphereOfInfluence jupiterSoi = addPlanetToSoiTree(jupiter, sunSoi);
        addPlanetToSoiTree(io, jupiterSoi);
        addPlanetToSoiTree(europa, jupiterSoi);
        addPlanetToSoiTree(ganymede, jupiterSoi);
        addPlanetToSoiTree(callisto, jupiterSoi);
    }

}
