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
                addRenderer(new UserOrbitalPointBitmapRenderer((PhysicalBody) dp));
                addRenderer(new PhysicalBodyOrbitPointsRenderer((PhysicalBody) dp));
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
                Spacecraft spacecraft = (Spacecraft) dp;
                addRenderer(new SpacecraftRenderer(spacecraft));
                addRenderer(new SpacecraftBitmapRenderer(spacecraft));

                addRenderer(new ApsidesRenderer(spacecraft));
                addRenderer(new ApoapsisBitmapRenderer(spacecraft));
                addRenderer(new PeriapsisBitmapRenderer(spacecraft));

                addRenderer(new NamedHistoryRenderer(spacecraft));
                addRenderer(new HistoryPointBitmapRenderer(spacecraft));

                //addRenderer(new TestingTextRenderer(spacecraft));

                addRenderer(new SpacecraftOrbitPointsRenderer(spacecraft));
                addRenderer(new OrbitIntersectionBitmapRenderer(spacecraft));
                addRenderer(new StartManeuverBitmapRenderer(spacecraft));
                addRenderer(new EndManeuverBitmapRenderer(spacecraft));

                addRenderer(new TargetTrajectoryRenderer(spacecraft));
            }
        }

        addRenderer(new ModelCameraPositionRenderer());
        addRenderer(new BackgroundRenderer());
        addRenderer(new TimeRenderer());
    }

}
