package com.momega.spacesimulator.renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.MovingObject;

/**
 * The class contains renderer for any abstract model
 * Created by martin on 4/19/14.
 */
public class ModelRenderer extends CompositeRenderer {

    private static final Logger logger = LoggerFactory.getLogger(ModelRenderer.class);

    public ModelRenderer() {
        logger.info("initializing renderers");
        createRenderers();
    }
    
    public void createRenderers() {
    	for(MovingObject mo : ModelHolder.getModel().getMovingObjects()) {
    		MovingObjectCompositeRenderer renderer = new MovingObjectCompositeRenderer(mo);
    		addRenderer(renderer);
        }
    }
    
    public Renderer deleteMovingObject(MovingObject mo) {
    	for(Renderer renderer : getRenderers()) {
    		if (renderer instanceof MovingObjectCompositeRenderer) {
    			MovingObjectCompositeRenderer mocr = (MovingObjectCompositeRenderer) renderer;
    			if (mo == mocr.getMovingObject()) {
    				removeRenderer(mocr);
    				return mocr;
    			}
    		}
    	}
    	return null;
    }

}
