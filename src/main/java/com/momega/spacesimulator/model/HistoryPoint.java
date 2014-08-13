package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/13/14.
 */
public class HistoryPoint extends NamedObject {

    private Vector3d position;
    private Timestamp timestamp;

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
