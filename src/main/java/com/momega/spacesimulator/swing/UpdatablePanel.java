package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.renderer.ModelChangeEvent;

/**
 * Created by martin on 8/24/14.
 */
public interface UpdatablePanel {

    void updateView(ModelChangeEvent event);
    
    void updateModel();
}
