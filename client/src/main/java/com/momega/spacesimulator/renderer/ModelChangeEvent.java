package com.momega.spacesimulator.renderer;

import java.io.Serializable;

import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;

/**
 * Created by martin on 8/12/14.
 */
public class ModelChangeEvent implements Serializable {

	private static final long serialVersionUID = 4492249704642213755L;
	private final Timestamp timestamp;
	private final Model model;
	
    /**
     * Constructs a prototypical Event.
     */
    public ModelChangeEvent(Model model) { 
    	this.model = model;
        this.timestamp = model.getTime();
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    
    public Model getModel() {
		return model;
	}
}
