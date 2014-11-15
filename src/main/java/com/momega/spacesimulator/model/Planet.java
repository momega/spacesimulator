package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The planet is the celestial body which might may have rings. Nowdays there is now other objects in our solar system with
 * the rings.
 * Created by martin on 7/11/14.
 */
public class Planet extends CelestialBody {

    private List<Ring> rings = new ArrayList<>();

    public List<Ring> getRings() {
        return rings;
    }

    public void setRings(List<Ring> rings) {
        this.rings = rings;
    }
}
