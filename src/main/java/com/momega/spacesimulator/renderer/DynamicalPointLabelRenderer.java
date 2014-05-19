package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.model.Satellite;
import com.momega.spacesimulator.model.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

/**
 * Created by martin on 4/28/14.
 */
public class DynamicalPointLabelRenderer extends AbstractTextRenderer {

    private static final Logger logger = LoggerFactory.getLogger(DynamicalPointLabelRenderer.class);

    private final DynamicalPoint dynamicalPoint;
    private final Camera camera;

    private double[] my2DPoint = new double[4];

    public DynamicalPointLabelRenderer(DynamicalPoint dynamicalPoint, Camera camera) {
        this.dynamicalPoint = dynamicalPoint;
        this.camera = camera;
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        double modelView[] = new double[16];
        double projection[] = new double[16];
         // will contain 2d window coordinates when done
        int viewport[] = new int[4];
        GL2 gl = drawable.getGL().getGL2();
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelView, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projection, 0 );
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0 );

        Vector3d viewVector = camera.getOrientation().getN();
        Vector3d diffVector = Vector3d.subtract(dynamicalPoint.getPosition(), camera.getPosition()).scale(1 / SCALE_FACTOR);

        if (viewVector.dot(diffVector) > 0) {  // object is in front of the camera
            GLU glu = new GLU();
            Vector3d p = dynamicalPoint.getPosition().scaled(1 / SCALE_FACTOR);
            glu.gluProject(p.x, p.y, p.z,
                    modelView, 0, projection, 0, viewport, 0, my2DPoint, 0);

            if (dynamicalPoint instanceof Satellite) {
                logger.debug("pos = {} x {}", my2DPoint[0], my2DPoint[1]);
                Satellite s = (Satellite) dynamicalPoint;
                logger.debug("modelView= {} ", modelView);
            }

            super.draw(drawable);
        }
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        setColor(255, 255, 255);
        drawText(dynamicalPoint.getName(), (int) my2DPoint[0] + 5, (int) my2DPoint[1] + 5);
        drawText(dynamicalPoint.getPosition().toString(), (int) my2DPoint[0] + 5, (int) my2DPoint[1] - 8);
    }
}
