package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.VectorUtils;

/**
 * Created by martin on 7/14/14.
 */
public class SimpleSolarSystemModelBuilder extends AbstractModelBuilder {

    protected CelestialBody sun;
    protected SphereOfInfluence sunSoi;

    @Override
    public void initPlanets() {
        sun = new CelestialBody();
        sun.setName("Sun");

        sun.setPosition(new Vector3d(0, 0, 0));
        sun.setVelocity(new Vector3d(0, 0, 0));
        createTrajectory(sun, new double[]{1, 0.7, 0}, TrajectoryType.STATIC);
        updateDynamicalPoint(sun, "Sun", 1.989 * 1E6, 25.05, 696.342, 286.13, 63.87);
        sun.setTextureFileName("sun.jpg");

        DynamicalPoint earthMoonBarycenter = new DynamicalPoint();
        createKeplerianElements(earthMoonBarycenter, sun, 149598.261d * 1E6, 0.0166739, 287.5824, 365.256814, 2456661.138788696378, 0.0018601064, 175.395d);
        updateDynamicalPoint(earthMoonBarycenter, "Earth-Moon Barycenter", 0, 0, 1, 0);
        createTrajectory(earthMoonBarycenter, new double[]{0, 0.5, 1}, TrajectoryType.KEPLERIAN);

        CelestialBody earth = new Planet();
        createKeplerianElements(earth, earthMoonBarycenter, 4.686955382086 * 1E6, 0.055557, 264.7609, 27.427302, 2456796.39770, 5.241500, 208.1199);
        updateDynamicalPoint(earth, "Earth", 5.97219, 0.997269, 6.371, 0d, 90d);
        createTrajectory(earth, new double[]{0, 0.5, 1}, TrajectoryType.KEPLERIAN);
        earth.setTextureFileName("earth.jpg");

        CelestialBody moon = new Planet();
        createKeplerianElements(moon, earthMoonBarycenter, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.241500, 208.1199);
        updateDynamicalPoint(moon, "Moon", 0.07349, 27.321, 1.737, 6.687);
        createTrajectory(moon, new double[]{0.5,0.5,0.5}, TrajectoryType.KEPLERIAN);
        moon.setTextureFileName("moon.jpg");

        addDynamicalPoint(sun);
        addDynamicalPoint(earthMoonBarycenter);
        addDynamicalPoint(earth);
        addDynamicalPoint(moon);

        sunSoi = addPlanetToSoiTree(sun, null);
        SphereOfInfluence earthSoi = addPlanetToSoiTree(earth, sunSoi, earthMoonBarycenter.getKeplerianElements());
        addPlanetToSoiTree(moon, earthSoi);

        model.setSelectedDynamicalPoint(earth);
    }

    @Override
    public void initSatellites() {
        CelestialBody earth = (CelestialBody) findDynamicalPoint("Earth");

//        Vector3d position = VectorUtils.fromSphericalCoordinates(200 * 1E3 + earth.getRadius(), Math.PI / 2, 0);
//        Orientation o = MathUtils.createOrientation(new Vector3d(0, 1d, 0), new Vector3d(0, 0, 1d));
//        Vector3d velocity = o.getN().scale(9000d);
//        Satellite satellite = createSatellite(earth, "Satellite 1", position, velocity);
//        addDynamicalPoint(satellite);
    }

}
