package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.service.ModelService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class contains renderer for any abstract model
 * Created by martin on 4/19/14.
 */
public class ModelRenderer extends CompositeRenderer {

    private static final Logger logger = LoggerFactory.getLogger(ModelRenderer.class);

	private ModelService modelService;

    public ModelRenderer() {
        logger.info("initializing renderers");
		modelService = Application.getInstance().getService(ModelService.class);
        createRenderers();
    }
    
    public void createRenderers() {
    	Model model = ModelHolder.getModel();
    	for(MovingObject mo : modelService.findAllMovingObjects(model)) {
    		MovingObjectCompositeRenderer renderer = new MovingObjectCompositeRenderer(mo);
    		addRenderer(renderer);
        }
		addRenderer(new BackgroundRenderer());
		addRenderer(new SurfacePointRenderer());
    }
    
    public MovingObjectCompositeRenderer deleteMovingObject(MovingObject mo) {
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
