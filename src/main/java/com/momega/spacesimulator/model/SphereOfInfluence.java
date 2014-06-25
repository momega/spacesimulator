package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Sphere of influence of the given body
 * Created by martin on 6/14/14.
 */
public class SphereOfInfluence {

    private CelestialBody body;
    private double radius;
    private List<SphereOfInfluence> children = new ArrayList<>();
    private SphereOfInfluence parent;

    public CelestialBody getBody() {
        return body;
    }

    public void setBody(CelestialBody body) {
        this.body = body;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public List<SphereOfInfluence> getChildren() {
        return children;
    }

    public void setChildren(List<SphereOfInfluence> children) {
        this.children = children;
    }

    public SphereOfInfluence getParent() {
        return parent;
    }

    public void setParent(SphereOfInfluence parent) {
        this.parent = parent;
    }
}
