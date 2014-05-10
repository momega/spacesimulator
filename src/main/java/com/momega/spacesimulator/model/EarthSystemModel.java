package com.momega.spacesimulator.model;

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
        earthTrajectory.setTrajectoryColor(new double[] {0,0.5,1});
        earth.setTrajectory(earthTrajectory);
        earth.setMass(5.97219 * 1E24);
        earth.setRotationPeriod(0.997269 * 86400);
        earth.setTextureFileName("earth.jpg");
        earth.setRadius(6.378 * 1E6);

                new StaticTrajectory(), time, 23.5, , , , ,  );


        moonTrajectory.setCentralObject(earth);
        moonTrajectory.setSemimajorAxis(384.399 * 1E6);

        Planet moon = new Planet("Moon",
                new KeplerianTrajectory3d(,,
                        0.055557,
                        84.7609,
                        27.427302, 2456796.39770989,
                        5.241500,
                        208.1199), time, 6.687, 1.737, 27.321, 0.07349, "moon.jpg",
                new double[] {0.5,0.5,0.5});

        addDynamicalPoint(earth);
        addDynamicalPoint(moon);

        Object3d satellitePosition = new Object3d(new Vector3d(6.378 + 0.2,0,0), new Vector3d(0, 9000d, 0), new Vector3d(0, 0, 1d));
        Satellite satellite = new Satellite("Satellite", new NewtonianTrajectory(getPlanets(), satellitePosition), time,  new double[] {1,1,1});
        addDynamicalPoint(satellite);
    }

    public KeplerianTrajectory3d createKeplerianTrajectory(DynamicalPoint centralObject, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double timeOfPeriapsis, double inclination, double ascendingNode) {
        KeplerianTrajectory3d moonTrajectory = new KeplerianTrajectory3d();
        this.inclination = Math.toRadians(inclination);
        this.ascendingNode = Math.toRadians(ascendingNode);
    }
}
