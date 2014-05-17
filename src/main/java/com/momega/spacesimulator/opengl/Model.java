package com.momega.spacesimulator.opengl;

import com.momega.spacesimulator.model.Camera;

/**
 * Created by martin on 5/17/14.
 */
public interface Model {

    /**
     * Gets the camera of the model
     * @return
     */
    Camera getCamera();

    /**
     * Performs next iteration
     */
    void next();
}
