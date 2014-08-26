package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/13/14.
 */
public interface PositionProvider {

    /**
     * Gets the position of the object
     * @return
     */
    Vector3d getPosition();

    /**
     * Gets the name of the position provider
     * @return
     */
    String getName();

    /**
     * Gets the timestamp of the position
     * @return
     */
    Timestamp getTimestamp();

}
