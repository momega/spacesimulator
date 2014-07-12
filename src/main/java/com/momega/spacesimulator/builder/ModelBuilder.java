package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.Model;

/**
 * Created by martin on 7/12/14.
 */
public interface ModelBuilder {

    /**
     * Initializes the builder
     * @return the instance of the model
     */
    Model init();
}
