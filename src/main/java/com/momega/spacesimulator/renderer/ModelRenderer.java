package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.AbstractModel;
import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.Satellite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class contains renderer for any abstract model
 * Created by martin on 4/19/14.
 */
public class ModelRenderer extends CompositeRenderer {

    private static final Logger logger = LoggerFactory.getLogger(ModelRenderer.class);

    private final AbstractModel model;

    public ModelRenderer(AbstractModel model) {
        this.model = model;

        logger.info("initializing renderers");
        for(DynamicalPoint dp : model.getUniverseService().getDynamicalPoints()) { // TODO: getting service from model it is not well
            DynamicalPointRenderer dpr;
            if (dp instanceof  Planet) {
                dpr = new PlanetRenderer((Planet) dp, model.getCamera());
            } else if (dp instanceof Satellite) {
                dpr = new SatelliteRenderer((Satellite) dp, model.getCamera());
            } else {
                dpr = new DynamicalPointRenderer(dp, model.getCamera());
            }

            addRenderer(dpr);
        }

        addRenderer(new CameraPositionRenderer(model.getCamera()));
        addRenderer(new TimeRenderer(model));
        addRenderer(new SelectedTargetRenderer(model));
    }

}
