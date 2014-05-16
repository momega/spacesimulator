package com.momega.spacesimulator.model;

/**
 * Created by martin on 5/8/14.
 */
public class SatelliteCamera extends Camera {

    private Satellite satellite;
    private double distance;

    public SatelliteCamera() {
    }

    @Override
    public void updatePosition() {
        setPosition(Vector3d.scaleAdd(distance, new Vector3d(1d, 0d, 0d), getSatellite().getPosition()));
    }

    public Satellite getSatellite() {
        return satellite;
    }

    public void setSatellite(Satellite satellite) {
        this.satellite = satellite;
    }

    public void changeDistance(double factor) {
        distance = distance * factor;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
