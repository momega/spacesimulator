package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class contains renderer for any abstract model
 * Created by martin on 4/19/14.
 */
public class ModelRenderer extends CompositeRenderer {

    private static final Logger logger = LoggerFactory.getLogger(ModelRenderer.class);

    private Model model;

    public ModelRenderer(Model model) {
        this.model = model;
        logger.info("initializing renderers");
        for(DynamicalPoint dp : model.getDynamicalPoints()) {
            addRenderer(new DynamicalPointRenderer(dp));
            if (dp instanceof  Planet) {
                addRenderer(new PlanetRenderer((Planet) dp));
            } else if (dp instanceof Satellite) {
                addRenderer(new SatelliteRenderer((Satellite) dp));
            }
        }

        addRenderer(new CameraPositionRenderer());
        addRenderer(new TimeRenderer());
        addRenderer(new SelectedTargetRenderer());
    }
    public Camera getCamera() {
        return model.getCamera();
    }

}
