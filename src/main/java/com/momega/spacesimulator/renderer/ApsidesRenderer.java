package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.KeplerianTrajectory;
import com.momega.spacesimulator.model.MovingObject;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.awt.*;

/**
 * Created by martin on 6/15/14.
 */
public class ApsidesRenderer extends AbstractTextRenderer {

    private final MovingObject movingObject;

    public ApsidesRenderer(MovingObject movingObject) {
        this.movingObject = movingObject;
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        ViewCoordinates bodyCoordinates = RendererModel.getInstance().findViewCoordinates(movingObject);
        if (bodyCoordinates != null && bodyCoordinates.isVisible()) {
            GL2 gl = drawable.getGL().getGL2();
            gl.glPointSize(8);
            gl.glColor3dv(movingObject.getTrajectory().getColor(), 0);
            KeplerianTrajectory keplerianTrajectory = movingObject.getTrajectory();
            gl.glBegin(GL2.GL_POINTS);
            if (keplerianTrajectory.getApoapsis() != null) {
                gl.glVertex3dv(keplerianTrajectory.getApoapsis().getPosition().asArray(), 0);
            }
            if (keplerianTrajectory.getPeriapsis() != null) {
                gl.glVertex3dv(keplerianTrajectory.getPeriapsis().getPosition().asArray(), 0);
            }
            gl.glEnd();
        }

        super.draw(drawable);
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        KeplerianTrajectory keplerianTrajectory = movingObject.getTrajectory();
        renderApsis(keplerianTrajectory.getApoapsis());
        renderApsis(keplerianTrajectory.getPeriapsis());
    }

    protected void renderApsis(Apsis apsis) {
        if (apsis == null) {
            return;
        }
        Point size = getTextSize(apsis.getName());
        ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(apsis);
        ViewCoordinates bodyCoordinates = RendererModel.getInstance().findViewCoordinates(movingObject);
        if (viewCoordinates!=null && viewCoordinates.isVisible()) {
            if (bodyCoordinates != null && bodyCoordinates.isVisible()) {
                drawText(apsis.getName(), viewCoordinates.getPoint().getX() - size.getX() / 2.0, viewCoordinates.getPoint().getY() - 16);
            }
        }
    }
}
