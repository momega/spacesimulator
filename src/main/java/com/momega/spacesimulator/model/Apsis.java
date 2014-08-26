package com.momega.spacesimulator.model;

/**
 * Created by martin on 6/29/14.
 */
public class Apsis extends NamedObject implements PositionProvider {

    private Vector3d position;
    private ApsisType type;
    private Timestamp timestamp;
    private KeplerianElements keplerianElements;

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public ApsisType getType() {
        return type;
    }

    public void setType(ApsisType type) {
        this.type = type;
    }

    public Timestamp getTimestamp() {
        return timestamp;
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
}
