package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Satellite;
import com.momega.spacesimulator.model.SphereOfInfluence;
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
        CelestialBody celestialBody = parentSoi.getBody();
        double distance = satellite.getCartesianState().getPosition().subtract(celestialBody.getCartesianState().getPosition()).length();
        if (distance > parentSoi.getRadius()) {
            return null;
        }
        return parentSoi;
    }

}
