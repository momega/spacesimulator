package com.momega.spacesimulator.renderer;

/**
 * Created by martin on 8/12/14.
 */
public interface ModelChangeListener {

    /**
     * The method is called whenever the model data are changed
     * @param event the event
     */
    void modelChanged(ModelChangeEvent event);
}
