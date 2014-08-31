package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.KeplerianTrajectory;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.opengl.GLUtils;

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
            drawBothApsis(gl, movingObject.getTrajectory());
        }

        super.draw(drawable);
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        KeplerianTrajectory keplerianTrajectory = movingObject.getTrajectory();
        renderApsis(keplerianTrajectory.getApoapsis());
        renderApsis(keplerianTrajectory.getPeriapsis());
    }

    protected void drawBothApsis(GL2 gl, KeplerianTrajectory keplerianTrajectory) {
        Apsis apoapsis = keplerianTrajectory.getApoapsis();
        drawApsis(gl, apoapsis, keplerianTrajectory.getColor());

        Apsis periapsis = keplerianTrajectory.getPeriapsis();
        drawApsis(gl, periapsis, keplerianTrajectory.getColor());
    }

    protected void drawApsis(GL2 gl, Apsis apsis, double color[]) {
        if (apsis != null && RendererModel.getInstance().isVisibleOnScreen(apsis)) {
            GLUtils.drawPoint(gl, 8, color, apsis);
        }
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
