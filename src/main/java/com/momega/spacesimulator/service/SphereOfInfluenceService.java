package com.momega.spacesimulator.service;

import com.momega.common.Tree;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * The service containing all operation regarding sphere of influence (SOI)
 * Created by martin on 6/14/14.
 */
@Component
public class SphereOfInfluenceService {

    public Tree<SphereOfInfluence> getSoiTree() {
        return ModelHolder.getModel().getSoiTree();
    }

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
        SphereOfInfluence soi = checkSoiOfPlanet(satellite, getSoiTree().getRoot());
        satellite.setSphereOfInfluence(soi);
        return soi;
    }

    protected SphereOfInfluence checkSoiOfPlanet(MovingObject satellite, SphereOfInfluence parentSoi) {
        for(SphereOfInfluence soi : getSoiTree().getChildren(parentSoi)) {
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

}
