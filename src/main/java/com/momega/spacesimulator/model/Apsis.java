package com.momega.spacesimulator.model;

/**
 * Created by martin on 6/29/14.
 */
public class Apsis extends NamedObject {

    private Vector3d position;

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    private ApsisType type;

    public ApsisType getType() {
        return type;
    }

    public void setType(ApsisType type) {
        this.type = type;
    }

}
