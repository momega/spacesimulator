package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.SphereOfInfluence;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by martin on 12/29/14.
 */
@Component
public class SoiMapCache {

    private Map<CelestialBody, SphereOfInfluence> soiMap = null;

    public CelestialBody get(CelestialBody celestialBody) {
        if (soiMap == null) {
            build();
        }
        SphereOfInfluence soi = soiMap.get(celestialBody);
        Assert.notNull(soi);
        return soi.getParent().getBody();
    }

    public synchronized void build() {
        soiMap = getAllChildren(ModelHolder.getModel().getRootSoi());
    }

    public synchronized void clear() {
        soiMap = null;
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
