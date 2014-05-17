package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.opengl.Renderer;

import javax.media.opengl.GL2;
import java.util.List;

/**
 * The class contains renderer for any abstract model
 * Created by martin on 4/19/14.
 */
public class ModelRenderer extends CompositeRenderer {

    private final AbstractModel model;

    public ModelRenderer(AbstractModel model) {
        this.model = model;
    }

    @Override
    protected void createRenderers(GL2 gl, List<Renderer> renderers) {
        for(DynamicalPoint dp : model.getDynamicalPoints()) {
            DynamicalPointRenderer dpr;
            if (dp instanceof  Planet) {
                dpr = new PlanetRenderer((Planet) dp, model.getCamera());
            } else if (dp instanceof Satellite) {
                dpr = new SatelliteRenderer((Satellite) dp, model.getCamera());
            } else {
                dpr = new DynamicalPointRenderer(dp, model.getCamera());
            }

            renderers.add(dpr);
        }

        renderers.add(new CameraPositionRenderer(model.getCamera()));
        renderers.add(new TimeRenderer(model.getTime()));
    }

}
