package com.momega.spacesimulator.renderer;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.model.Vector3d;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import java.awt.*;

/**
 * Created by martin on 4/28/14.
 */
public class DynamicalPointRenderer extends ObjectRenderer {

    private final DynamicalPoint dynamicalPoint;
    private final Camera camera;
    private final TrajectoryRenderer trajectoryRenderer;
    private TextRenderer textRenderer;

    public DynamicalPointRenderer(DynamicalPoint dynamicalPoint, Camera camera) {
        this.trajectoryRenderer = TrajectoryRenderer.createInstance(dynamicalPoint);
        this.dynamicalPoint = dynamicalPoint;
        this.camera = camera;
    }

    @Override
    public void init(GL2 gl) {
        this.textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12));
    }

    @Override
    public void draw(GL2 gl) {
        drawLabel(gl);
        trajectoryRenderer.draw(gl);
    }

    @Override
    public void dispose(GL2 gl) {
        textRenderer.dispose();
    }

    protected void drawLabel(GL2 gl) {
        double modelView[] = new double[16];
        double projection[] = new double[16];
        double[] my2DPoint = new double[4]; // will contain 2d window coordinates when done
        int viewport[] = new int[4];
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelView, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projection, 0 );
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0 );

        Vector3d viewVector = camera.getOrientation().getN();
        Vector3d diffVector = camera.getPosition().clone().negate().add(dynamicalPoint.getPosition());

        if (viewVector.dot(diffVector) > 0) {  // object is in front of the camera
            GLU glu = new GLU();
            glu.gluProject(dynamicalPoint.getPosition().x, dynamicalPoint.getPosition().y, dynamicalPoint.getPosition().z,
                            modelView, 0, projection, 0, viewport, 0, my2DPoint, 0);

            textRenderer.beginRendering(viewport[2], viewport[3]);
            textRenderer.setColor(1, 1, 1, 1);
            textRenderer.draw(dynamicalPoint.getName(), (int) my2DPoint[0] + 5, (int) my2DPoint[1] + 5);
            textRenderer.endRendering();
        }
    }

}
