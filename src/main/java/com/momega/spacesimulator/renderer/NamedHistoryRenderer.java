package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.Spacecraft;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.awt.*;

/**
 * Created by martin on 8/24/14.
 */
public class NamedHistoryRenderer extends AbstractTextRenderer {

    private final Spacecraft spacecraft;

    public NamedHistoryRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPointSize(8);
        gl.glColor3dv(this.spacecraft.getHistoryTrajectory().getColor(), 0);

        gl.glBegin(GL2.GL_POINTS);
        for(HistoryPoint hp : spacecraft.getHistoryTrajectory().getNamedHistoryPoints()) {
            gl.glVertex3dv(hp.getPosition().asArray(), 0);
        }
        gl.glEnd();

        super.draw(drawable);
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        for(HistoryPoint hp : spacecraft.getHistoryTrajectory().getNamedHistoryPoints()) {
            renderHistoryPoint(hp);
        }
    }

    protected void renderHistoryPoint(HistoryPoint historyPoint) {
        if (historyPoint == null) {
            return;
        }
        Point size = getTextSize(historyPoint.getName());
        ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(historyPoint);
        if (viewCoordinates.isVisible()) {
            drawText(historyPoint.getName(), viewCoordinates.getPoint().getX() - size.getX() / 2.0, viewCoordinates.getPoint().getY() - 16);
        }
    }
}
