package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.DynamicalPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by martin on 4/28/14.
 */
public class DynamicalPointRenderer extends CompositeRenderer {

    private final TrajectoryRenderer trajectoryRenderer;
    private final DynamicalPointLabelRenderer labelRenderer;

    public DynamicalPointRenderer(DynamicalPoint dynamicalPoint) {
        this.trajectoryRenderer = TrajectoryRenderer.createInstance(dynamicalPoint.getTrajectory());
        this.labelRenderer = new DynamicalPointLabelRenderer(dynamicalPoint);

        addRenderer(trajectoryRenderer);
        addRenderer(labelRenderer);
    }

}
