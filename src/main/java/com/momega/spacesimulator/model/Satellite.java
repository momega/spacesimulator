package com.momega.spacesimulator.model;

/**
 * Created by martin on 5/5/14.
 */
public class Satellite extends DynamicalPoint {

    private SphereOfInfluence sphereOfInfluence;
    private Vector3d relativePosition;
    private Vector3d relativeVelocity;

    public Vector3d getRelativePosition() {
        return relativePosition;
    }

    public void setRelativePosition(Vector3d relativePosition) {
        this.relativePosition = relativePosition;
    }

    public Vector3d getRelativeVelocity() {
        return relativeVelocity;
    }

    public void setRelativeVelocity(Vector3d relativeVelocity) {
        this.relativeVelocity = relativeVelocity;
    }

    public SphereOfInfluence getSphereOfInfluence() {
        return sphereOfInfluence;
    }

    public void setSphereOfInfluence(SphereOfInfluence sphereOfInfluence) {
        this.sphereOfInfluence = sphereOfInfluence;
    }
}
