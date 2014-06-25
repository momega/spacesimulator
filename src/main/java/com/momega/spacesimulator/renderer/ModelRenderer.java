package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class contains renderer for any abstract model
 * Created by martin on 4/19/14.
 */
public class ModelRenderer extends CompositeRenderer {

    private static final Logger logger = LoggerFactory.getLogger(ModelRenderer.class);

    public ModelRenderer() {
        logger.info("initializing renderers");
        for(DynamicalPoint dp : ModelHolder.getModel().getDynamicalPoints()) {
            addRenderer(new DynamicalPointRenderer(dp));
            if (dp instanceof CelestialBody) {
                addRenderer(new CelestialBodyRenderer((CelestialBody) dp));
            } else if (dp instanceof Satellite) {
                addRenderer(new SatelliteRenderer((Satellite) dp));
            }
        }

        addRenderer(new CameraPositionRenderer());
        addRenderer(new TimeRenderer());
        addRenderer(new SelectedTargetRenderer());
    }

}
