package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;

import java.util.EventObject;

/**
 * Created by martin on 8/12/14.
 */
public class ModelChangeEvent extends EventObject {

    private Timestamp timestamp;

    /**
     * Constructs a prototypical Event.
     *
     * @param model the model
     * @throws IllegalArgumentException if source is null.
     */
    public ModelChangeEvent(Model model) {
        super(model);
        this.timestamp = model.getTime();
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
