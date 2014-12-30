package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.SphereOfInfluence;
import org.springframework.stereotype.Component;

/**
 * The builder of the solar system
 * Created by martin on 5/6/14.
 */
@Component
public class SolarSystemModelBuilder extends MediumSolarSystemModelBuilder {

    @Override
    public void initPlanets() {
        super.initPlanets();

        CelestialBody mercury = new Planet();
        updateDynamicalPoint(mercury, "Mercury", 0.3302, 58.646, 2.4397, 281.01, 61.45, 329.5469, "Mercury_(planet)", null);
        createKeplerianElements(mercury, sun, 57909.05d * 1E6, 0.20563, 29.124, 87.96890, 2456780.448693044949, 7.0, 48.313);
        createTrajectory(mercury, new double[]{0.2, 0.2, 0.2});
        mercury.setTextureFileName("mercury.jpg");

        CelestialBody jupiter = new Planet();
        updateDynamicalPoint(jupiter, "Jupiter", 1898.13, 9.925d / 24, 69.911, 268.05, 64.49, 284.95, "Jupiter_(planet)", null);
        createKeplerianElements(jupiter, sun, 778547.2d * 1E6, 0.048775, 274.008653, 4332.59, 2455638.655976880342, 1.303541, 100.5118);
        createTrajectory(jupiter, new double[]{1,0.65,0.0});
        jupiter.setTextureFileName("jupiter.jpg");

        addMovingObject(mercury);
        addMovingObject(jupiter);

        CelestialBody io = new CelestialBody();
        updateDynamicalPoint(io, "Io",  893.3E-4, 1.769138, 1.8213, 0d, "Io_(moon)", null);
        createKeplerianElements(io, jupiter, 421.769 * 1E6, 0.0041, 136.11730, 1.769138, 2456821.035697427578, 2.24147, 337.181);
        createTrajectory(io, new double[]{1, 1, 1});
        io.setTextureFileName("io.jpg");
        addMovingObject(io);

        CelestialBody europa = new CelestialBody();
        updateDynamicalPoint(europa, "Europa", 479.7E-4, 3.551810, 1.565, 0.1d, "Europa_(moon)", null);
        createKeplerianElements(europa, jupiter, 671.079 * 1E6, 0.0101, 302.75, 3.551810, 2456822.782242114656, 2.62907342, 343.685);
        createTrajectory(europa, new double[]{1, 1, 1});
        europa.setTextureFileName("europa.jpg");
        addMovingObject(europa);

        CelestialBody ganymede = new CelestialBody();
        updateDynamicalPoint(ganymede, "Ganymede", 1482E-4, 7.154553, 2.634, 0.33d, null, null);
        createKeplerianElements(ganymede, jupiter, 1070.0428 * 1E6, 0.0006, 342.14, 7.154553, 2456819.953914982267, 2.2829570, 340.928);
        createTrajectory(ganymede, new double[]{1, 1, 1});
        ganymede.setTextureFileName("ganymede.jpg");
        addMovingObject(ganymede);

        CelestialBody callisto = new CelestialBody();
        updateDynamicalPoint(callisto, "Callisto", 1076E-4, 16.689018, 2.403, 0.22d, null, null);
        createKeplerianElements(callisto, jupiter, 1883 * 1E6, 0.007, 263.79, 16.689018, 2456815.215215998702, 1.9812553570, 337.0061);
        createTrajectory(callisto, new double[]{1, 1, 1});
        callisto.setTextureFileName("callisto.jpg");
        addMovingObject(callisto);

        SphereOfInfluence jupiterSoi = addPlanetToSoiTree(jupiter, sunSoi);
        addPlanetToSoiTree(io, jupiterSoi);
        addPlanetToSoiTree(europa, jupiterSoi);
        addPlanetToSoiTree(ganymede, jupiterSoi);
        addPlanetToSoiTree(callisto, jupiterSoi);
    }

    @Override
    public String getName() {
        return "Medium Solar System model with Jupiter";
    }

}
