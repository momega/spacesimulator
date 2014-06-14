package com.momega.spacesimulator.model;

/**
 * Represents the
 * Created by martin on 6/14/14.
 */
public class SphereOfInfluence {

    private Planet body;
    private double radius;

    public Planet getBody() {
        return body;
    }

    public void setBody(Planet body) {
        this.body = body;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
