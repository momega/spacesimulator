package com.momega.spacesimulator.model;

/**
 * Interface for all object which are located on the screen
 * Created by martin on 8/13/14.
 */
public interface PositionProvider {

    /**
     * Gets the position of the object
     * @return the position in [x,y,z]
     */
    Vector3d getPosition();

    /**
     * Gets the name of the position provider
     * @return the name of the object
     */
    String getName();

    /**
     * Gets the timestamp of the position
     * @return the timestamp of the position provider
     */
    Timestamp getTimestamp();

}
