package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.DynamicalPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by martin on 4/28/14.
 */
public class DynamicalPointRenderer extends CompositeRenderer {

    private static final Logger logger = LoggerFactory.getLogger(DynamicalPointRenderer.class);

    private final TrajectoryRenderer trajectoryRenderer;
    private final DynamicalPointLabelRenderer labelRenderer;
    private final DynamicalPoint dynamicalPoint;

    public DynamicalPointRenderer(DynamicalPoint dynamicalPoint, Camera camera) {
        this.dynamicalPoint = dynamicalPoint;
        this.trajectoryRenderer = TrajectoryRenderer.createInstance(dynamicalPoint.getTrajectory(), camera);
        this.labelRenderer = new DynamicalPointLabelRenderer(dynamicalPoint);

        addRenderer(trajectoryRenderer);
        addRenderer(labelRenderer);
    }

}
