package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 10/19/14.
 */
public class Target {

    private CelestialBody targetBody;
    private List<OrbitIntersection> orbitIntersections = new ArrayList<>();
    private double angle;

    public CelestialBody getTargetBody() {
        return targetBody;
    }

    public void setTargetBody(CelestialBody targetBody) {
        this.targetBody = targetBody;
    }

    public List<OrbitIntersection> getOrbitIntersections() {
        return orbitIntersections;
    }

    public void setOrbitIntersections(List<OrbitIntersection> orbitIntersections) {
        this.orbitIntersections = orbitIntersections;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
