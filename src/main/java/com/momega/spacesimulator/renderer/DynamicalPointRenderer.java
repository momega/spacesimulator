package com.momega.spacesimulator.renderer;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.model.Satellite;
import com.momega.spacesimulator.model.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.*;

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
        this.trajectoryRenderer = TrajectoryRenderer.createInstance(dynamicalPoint.getTrajectory());
        this.labelRenderer = new DynamicalPointLabelRenderer(dynamicalPoint, camera);

        addRenderer(trajectoryRenderer);
        addRenderer(labelRenderer);
    }

}
