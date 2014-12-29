package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.SphereOfInfluence;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * The service containing all operation regarding sphere of influence (SOI)
 * Created by martin on 6/14/14.
 */
@Component
public class SphereOfInfluenceService {

    private Map<CelestialBody, SphereOfInfluence> soiMap = null;

    public SphereOfInfluence findCurrentSoi(PositionProvider positionProvider) {
        SphereOfInfluence soi = checkSoiOfPlanet(positionProvider, ModelHolder.getModel().getRootSoi());
        return soi;
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
        if (soiMap == null) {
            buildSoiMap();
        }
        SphereOfInfluence soi = soiMap.get(celestialBody);
        Assert.notNull(soi);
        return soi.getParent().getBody();
    }

    protected synchronized void buildSoiMap() {
        soiMap = getAllChildren(ModelHolder.getModel().getRootSoi());
    }

    protected Map<CelestialBody, SphereOfInfluence> getAllChildren(SphereOfInfluence parentSoi) {
        Map<CelestialBody, SphereOfInfluence> result = new HashMap<>();
        result.put(parentSoi.getBody(), parentSoi);
        for(SphereOfInfluence soi : parentSoi.getChildren()) {
            result.putAll(getAllChildren(soi));
        }
        return result;
    }

}
