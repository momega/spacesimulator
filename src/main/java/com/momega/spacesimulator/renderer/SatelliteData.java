package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.SphereOfInfluence;
import com.momega.spacesimulator.model.Vector3d;

/**
 * Created by martin on 6/23/14.
 */
public class SatelliteData {

    private SphereOfInfluence sphereOfInfluence;

    public SphereOfInfluence getSphereOfInfluence() {
        return sphereOfInfluence;
    }

    public void setSphereOfInfluence(SphereOfInfluence sphereOfInfluence) {
        this.sphereOfInfluence = sphereOfInfluence;
    }
}
