package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.model.Satellite;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.util.List;

/**
 * Created by martin on 4/28/14.
 */
public class DynamicalPointRenderer extends CompositeRenderer {

    private final TrajectoryRenderer trajectoryRenderer;
    private final DynamicalPointLabelRenderer labelRenderer;

    public DynamicalPointRenderer(DynamicalPoint dynamicalPoint, Camera camera) {
        this.trajectoryRenderer = TrajectoryRenderer.createInstance(dynamicalPoint.getTrajectory());
        this.labelRenderer = new DynamicalPointLabelRenderer(dynamicalPoint, camera);
    }

    @Override
    protected void createRenderers(GL2 gl, List<Renderer> renderers) {
        renderers.add(this.trajectoryRenderer);
        renderers.add(this.labelRenderer);
    }
}
