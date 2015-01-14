package com.momega.spacesimulator.context;

import com.momega.spacesimulator.model.Model;

/**
 * The holder of the model instance
 * Created by martin on 6/18/14.
 */
public final class ModelHolder {

    private static Model model = null;

    public static Model getModel() {
        return model;
    }
    
    public static void setModel(Model newModel) {
    	model = newModel;
    }

}
