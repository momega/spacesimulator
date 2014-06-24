package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Component;

/**
 * The service containing all operation regarding sphere of influence (SOI)
 * Created by martin on 6/14/14.
 */
@Component
public class SphereOfInfluenceService {

    public SphereOfInfluence findCurrentSoi(Satellite satellite) {
        SphereOfInfluence soi = checkSoiOfPlanet(satellite, ModelHolder.getModel().getRootSoi());
        return soi;
    }

    protected SphereOfInfluence checkSoiOfPlanet(MovingObject satellite, SphereOfInfluence parentSoi) {
        for(SphereOfInfluence soi : parentSoi.getChildren()) {
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

    public SphereOfInfluence findByPredicate(Predicate predicate) {
        return findByPredicate(ModelHolder.getModel().getRootSoi(), predicate);
    }

    public SphereOfInfluence findByPredicate(SphereOfInfluence item, Predicate predicate) {
        if (predicate.evaluate(item)) {
            return item;
        }
        for(SphereOfInfluence child : item.getChildren()) {
            SphereOfInfluence result =  findByPredicate(child, predicate);
            if (result != null) {
                return child;
            }
        }
        return null;
    }
}
