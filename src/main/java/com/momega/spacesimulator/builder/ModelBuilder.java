package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.Model;

/**
 * Created by martin on 7/12/14.
 */
public interface ModelBuilder {

    /**
     * Returns the name of the builder
     * @return
     */
    String getName();

    /**
     * Builds the model
     * @return the instance of the model
     */
    Model build();
}
