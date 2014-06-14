package com.momega.spacesimulator.service;

import com.momega.common.Tree;
import com.momega.spacesimulator.model.*;

/**
 * The service containing all operation regarding sphere of influence (SOI)
 * Created by martin on 6/14/14.
 */
public class SphereOfInfluenceService {

    private UniverseService universeService;

    private Tree<Planet> soiTree = new Tree<>();

    public SphereOfInfluence findCurrentSoi(MovingObject satellite) {
        SphereOfInfluence result = new SphereOfInfluence();
        Planet soiBody = checkSoiOfPlanet(satellite, soiTree.getRoot());
        result.setBody(soiBody);
        result.setRadius(soiBody.getSoiRadius());
        return result;
    }

    protected Planet checkSoiOfPlanet(MovingObject satellite, Planet planet) {
        for(Planet moon : soiTree.getChildren(planet)) {
            Planet soiBody = checkSoiOfPlanet(satellite, moon);
            if (soiBody != null) {
                return soiBody;
            }
        }
        double distance = satellite.getPosition().subtract(planet.getPosition()).length();
        if (distance > planet.getSoiRadius()) {
            return null;
        }
        return planet;
    }

    /**
     * Calculates the radius of the SOI (Sphere of influence)
     * @param planet the planet
     */
    public void calculatePlanetSoi(Planet planet) {
        if (planet == universeService.getCentralBody()) {
            planet.setSoiRadius(UniverseService.AU * 100);
        } else {
            Planet centralBody = soiTree.getParent(planet);
            KeplerianTrajectory2d planetTrajectory = (KeplerianTrajectory2d) planet.getTrajectory();
            double radius = Math.pow(planet.getMass() / centralBody.getMass(), 0.4d) * planetTrajectory.getSemimajorAxis();
            planet.setSoiRadius(radius);
        }
    }

    /**
     * Calculate soi for all the planets
     */
    public void calculateSois() {
        for(Planet p : universeService.getPlanets()) {
            calculatePlanetSoi(p);
        }
    }

    public void addPlanet(Planet planet, Planet centralPlanet) {
        soiTree.add(planet, centralPlanet);
    }

    public void setUniverseService(UniverseService universeService) {
        this.universeService = universeService;
    }
}
