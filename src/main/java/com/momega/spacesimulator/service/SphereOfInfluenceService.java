package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.SphereOfInfluence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The service containing all operation regarding sphere of influence (SOI)
 * Created by martin on 6/14/14.
 */
@Component
public class SphereOfInfluenceService {

    @Autowired
    private SoiMapCache soiMap;

    public SphereOfInfluence findCurrentSoi(PositionProvider positionProvider) {
        return checkSoiOfPlanet(positionProvider, ModelHolder.getModel().getRootSoi());
    }

    protected SphereOfInfluence checkSoiOfPlanet(PositionProvider positionProvider, SphereOfInfluence parentSoi) {
        for(SphereOfInfluence soi : parentSoi.getChildren()) {
            SphereOfInfluence childSoi = checkSoiOfPlanet(positionProvider, soi);
            if (childSoi != null) {
                return childSoi;
            }
        }
        CelestialBody celestialBody = parentSoi.getBody();
        double distance = positionProvider.getPosition().subtract(celestialBody.getCartesianState().getPosition()).length();
        if (distance > parentSoi.getRadius()) {
            return null;
        }
        return parentSoi;
    }

    public CelestialBody findParentBody(CelestialBody celestialBody) {
        return soiMap.get(celestialBody);
    }

}
