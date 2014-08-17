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
        for(PhysicalBody dp : ModelHolder.getModel().getPhysicalBodies()) {
            if (!TrajectoryType.STATIC.equals(dp.getTrajectory().getType())) {
                addRenderer(new KeplerianTrajectoryRenderer(dp));
            }
            if (dp instanceof CelestialBody) {
                addRenderer(new CelestialBodyRenderer((CelestialBody) dp));
                if (dp instanceof Planet) {
                    Planet p = (Planet) dp;
                    for(Ring ring : p.getRings()) {
                        addRenderer(new PlanetRingRenderer(p, ring));
                    }
                }
            } else if (dp instanceof Spacecraft) {
                addRenderer(new SpacecraftRenderer((Spacecraft) dp));
                addRenderer(new HistoryRenderer((Spacecraft) dp));
                addRenderer(new ApsidesRenderer((Spacecraft) dp));
            }
            addRenderer(new PhysicalBodyRenderer(dp));
        }

        addRenderer(new CameraPositionRenderer());
        //addRenderer(new BackgroundRenderer());
        addRenderer(new TimeRenderer());
        //addRenderer(new SelectedTargetRenderer());
    }

}
