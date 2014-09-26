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
        for(MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
            if (!TrajectoryType.STATIC.equals(dp.getTrajectory().getType())) {
                addRenderer(new KeplerianTrajectoryRenderer(dp));
            }
            if (dp instanceof PhysicalBody) {
                addRenderer(new MovingObjectRenderer(dp));
                addRenderer(new ApsidesRenderer(dp));
            }
            if (dp instanceof BaryCentre) {
            	addRenderer(new MovingObjectRenderer(dp));
                addRenderer(new ApsidesRenderer(dp));
            }
            if (dp instanceof CelestialBody) {
            	addRenderer(new MovingObjectRenderer(dp));
                addRenderer(new CelestialBodyRenderer((CelestialBody) dp, true, true));
                if (dp instanceof Planet) {
                    Planet p = (Planet) dp;
                    for(Ring ring : p.getRings()) {
                        addRenderer(new PlanetRingRenderer(p, ring));
                    }
                }
            } else if (dp instanceof Spacecraft) {
                addRenderer(new SpacecraftRenderer((Spacecraft) dp, true));
                addRenderer(new HistoryRenderer((Spacecraft) dp));
                addRenderer(new ApsidesRenderer(dp));
                addRenderer(new NamedHistoryRenderer((Spacecraft) dp));
                addRenderer(new OrbitIntersectionRenderer((Spacecraft) dp));
            }
        }

        addRenderer(new ModelCameraPositionRenderer());
        addRenderer(new TimeRenderer());
    }

}
