package com.momega.spacesimulator.model;

/**
 * Created by martin on 5/6/14.
 */
public class EarthSystemModel extends AbstractModel {

    @Override
    public void initDynamicalPoints() {

        Planet earth = new Planet("Earth",
                new StaticTrajectory(new Vector3d(0,0,0)), time, 23.5, 6.378, 0.997269, 5.97219, "earth.jpg",  new double[] {0,0.5,1});

        Planet moon = new Planet("Moon",
                new KeplerianTrajectory3d(earth, 384.399,
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
}
