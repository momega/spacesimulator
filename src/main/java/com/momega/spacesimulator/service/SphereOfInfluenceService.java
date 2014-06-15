package com.momega.spacesimulator.service;

import com.momega.common.Tree;
import com.momega.spacesimulator.model.*;
import org.apache.commons.collections.Predicate;
import org.springframework.util.Assert;

/**
 * The service containing all operation regarding sphere of influence (SOI)
 * Created by martin on 6/14/14.
 */
public class SphereOfInfluenceService {

    private Tree<SphereOfInfluence> soiTree = new Tree<>();

    /**
     * Updates the relative position and velocity to the body of the sphere of influence
     * @param satellite the satellite
     */
    public void updateRelativePositionAndVelocity(Satellite satellite) {
        Planet soiPlanet = satellite.getSphereOfInfluence().getBody();
        Vector3d position = satellite.getPosition().subtract(soiPlanet.getPosition());
        Vector3d velocity = satellite.getVelocity().subtract(soiPlanet.getVelocity());
        satellite.setRelativePosition(position);
        satellite.setRelativeVelocity(velocity);
    }

    public SphereOfInfluence findCurrentSoi(Satellite satellite) {
        SphereOfInfluence soi = checkSoiOfPlanet(satellite, soiTree.getRoot());
        satellite.setSphereOfInfluence(soi);
        return soi;
    }

    protected SphereOfInfluence checkSoiOfPlanet(MovingObject satellite, SphereOfInfluence parentSoi) {
        for(SphereOfInfluence soi : soiTree.getChildren(parentSoi)) {
            SphereOfInfluence childSoi = checkSoiOfPlanet(satellite, soi);
            if (childSoi != null) {
                return childSoi;
            }
        }
        Planet planet = parentSoi.getBody();
        double distance = satellite.getPosition().subtract(planet.getPosition()).length();
        if (distance > parentSoi.getRadius()) {
            return null;
        }
        return parentSoi;
    }

    /**
     * The method adds the planet to the SOI tree and calculate the radius of the planet soi. The trajectory comes directly
     * from the planet trajectory
     * @param planet the planet
     * @param centralPlanet the central planet
     */
    public void addPlanet(final Planet planet, final Planet centralPlanet) {
        if (centralPlanet == null) {
            SphereOfInfluence soi = new SphereOfInfluence();
            soi.setBody(planet);
            soi.setRadius(UniverseService.AU * 100);
            soiTree.add(soi, null);
        } else {
            Assert.isInstanceOf(KeplerianTrajectory2d.class, planet.getTrajectory());
            addPlanet(planet, centralPlanet, (KeplerianTrajectory2d) planet.getTrajectory());
        }
    }

    /**
     * The method adds the planet to the SOI tree and calculate the radius of the planet soi.
     * @param planet the planet
     * @param centralPlanet the central planet
     * @param trajectory the trajectory of the planet. It has to be specified when the the planet
     *                   orbiting the bary-centre. In these cases it is not possible to calculate correctly sphere of influence. The example is
     *                   Earth -> (Earth/Moon Barycentre) -> Sun
     */
    public void addPlanet(final Planet planet, final Planet centralPlanet, KeplerianTrajectory2d trajectory) {
        SphereOfInfluence soi = new SphereOfInfluence();
        double radius = Math.pow(planet.getMass() / centralPlanet.getMass(), 0.4d) * trajectory.getSemimajorAxis();
        soi.setRadius(radius);
        soi.setBody(planet);
        SphereOfInfluence parentSoi = soiTree.findByPredicate(new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                SphereOfInfluence obj = (SphereOfInfluence) object;
                return (obj.getBody() == centralPlanet);
            }
        });
        soiTree.add(soi, parentSoi);
    }
}
