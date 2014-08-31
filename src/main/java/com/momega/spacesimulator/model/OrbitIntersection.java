package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/31/14.
 */
public class OrbitIntersection extends NamedObject implements PositionProvider  {

    private Vector3d position;
    private Timestamp timestamp;
    private KeplerianElements keplerianElements;
    private MovingObject targetObject;

    @Override
    public Vector3d getPosition() {
        return position;
    }

    @Override
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public KeplerianElements getKeplerianElements() {
        return keplerianElements;
    }

    public void setKeplerianElements(KeplerianElements keplerianElements) {
        this.keplerianElements = keplerianElements;
    }

    public MovingObject getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(MovingObject targetObject) {
        this.targetObject = targetObject;
    }
}
