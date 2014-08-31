package com.momega.spacesimulator.model;

/**
 * Apsis of the trajectory for any {@link com.momega.spacesimulator.model.KeplerianTrajectory}
 * Created by martin on 6/29/14.
 */
public class Apsis extends NamedObject implements PositionProvider {

    private Vector3d position;
    private ApsisType type;
    private Timestamp timestamp;
    private KeplerianElements keplerianElements;
    private boolean visible;

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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
