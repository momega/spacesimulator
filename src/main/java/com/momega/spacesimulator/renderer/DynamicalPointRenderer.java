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

    private final DynamicalPoint dynamicalPoint;
    private final Camera camera;
    private final TrajectoryRenderer trajectoryRenderer;
    private TextRenderer textRenderer;

    public DynamicalPointRenderer(DynamicalPoint dynamicalPoint, Camera camera) {
        this.trajectoryRenderer = TrajectoryRenderer.createInstance(dynamicalPoint.getTrajectory());
        this.dynamicalPoint = dynamicalPoint;
        this.camera = camera;

        addRenderer(trajectoryRenderer);
    }

    @Override
    public void init(GL2 gl) {
        super.init(gl);
        this.textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12));
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        super.draw(drawable);
        GL2 gl = drawable.getGL().getGL2();
        drawLabel(gl);
    }

    @Override
    public void dispose(GL2 gl) {
        super.dispose(gl);
        textRenderer.dispose();
    }

    /**
     * Helper method to draw the label for th given object
     * @param gl
     */
    protected void drawLabel(GL2 gl) {
        double modelView[] = new double[16];
        double projection[] = new double[16];
        double[] my2DPoint = new double[4]; // will contain 2d window coordinates when done
        int viewport[] = new int[4];
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelView, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projection, 0 );
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0 );

        Vector3d viewVector = camera.getOrientation().getN();
        Vector3d diffVector = camera.getPosition().clone().negate().add(dynamicalPoint.getPosition()).scale(1 / SCALE_FACTOR);

        if (viewVector.dot(diffVector) > 0) {  // object is in front of the camera
            GLU glu = new GLU();
            Vector3d p = dynamicalPoint.getPosition().scaled(1 / SCALE_FACTOR);
            glu.gluProject(p.x, p.y, p.z,
                            modelView, 0, projection, 0, viewport, 0, my2DPoint, 0);

            textRenderer.beginRendering(viewport[2], viewport[3]);

            if (dynamicalPoint instanceof Satellite) {
                logger.debug("pos = {} x {}", my2DPoint[0], my2DPoint[1]);
                Satellite s = (Satellite) dynamicalPoint;
                logger.debug("modelView= {} ", modelView);
            }

            textRenderer.setColor(1, 1, 1, 1);
            textRenderer.draw(dynamicalPoint.getName(), (int) my2DPoint[0] + 5, (int) my2DPoint[1] + 5);
            textRenderer.endRendering();
        }
    }

}
