package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.opengl.GLUtils;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.awt.*;

/**
 * Renderer displays the point at the dynamical point position.
 * Created by martin on 4/28/14.
 */
public class DynamicalPointRenderer extends AbstractTextRenderer {

    private final DynamicalPoint dynamicalPoint;

    public DynamicalPointRenderer(DynamicalPoint dynamicalPoint) {
        this.dynamicalPoint = dynamicalPoint;
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();

        GLUtils.translate(gl, dynamicalPoint.getPosition());

        gl.glBegin(GL2.GL_POINTS);
        gl.glColor3dv(dynamicalPoint.getTrajectory().getColor(), 0);
        gl.glPointSize(6);
        gl.glVertex3dv(new double[] {0d, 0d, 0d}, 0);
        gl.glEnd();

        gl.glPopMatrix();

        super.draw(drawable);
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(dynamicalPoint);
        if (viewCoordinates.getRadius()<=5 && viewCoordinates.isVisible()) {
            Point size = getTextSize(dynamicalPoint.getName());
            drawText(dynamicalPoint.getName(), viewCoordinates.getPoint().getX() - size.getX() / 2.0, viewCoordinates.getPoint().getY() - 16);
        }
    }

}
