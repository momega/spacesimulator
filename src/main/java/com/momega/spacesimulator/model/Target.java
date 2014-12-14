package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The class represents single target of the spacecraft. It contains the target body, list of the planes intersections between spacecraft orbit
 * and the target body orbital plane.
 * It also contains the angle between planes.
 * Created by martin on 10/19/14.
 */
public class Target {

    private transient CelestialBody targetBody;
    private List<OrbitIntersection> orbitIntersections = new ArrayList<>();
    private transient Double angle = null;
    private transient Double distance = null;
    private KeplerianElements keplerianElements;

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

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }
    
    public void setDistance(Double distance) {
		this.distance = distance;
	}
    
    public Double getDistance() {
		return distance;
	}

    public KeplerianElements getKeplerianElements() {
        return keplerianElements;
    }

    public void setKeplerianElements(KeplerianElements keplerianElements) {
        this.keplerianElements = keplerianElements;
    }
}
