package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.Satellite;
import com.momega.spacesimulator.model.SatelliteTrajectory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.awt.*;

/**
 * Created by martin on 6/15/14.
 */
public class ApsidesRenderer extends AbstractTextRenderer {

    private final Satellite satellite;

    public ApsidesRenderer(Satellite satellite) {
        this.satellite = satellite;
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPointSize(8);
        gl.glColor3dv(this.satellite.getTrajectory().getColor(), 0);
        SatelliteTrajectory satelliteTrajectory = (SatelliteTrajectory) satellite.getTrajectory();
        gl.glBegin(GL2.GL_POINTS);
        if (satelliteTrajectory.getApoapsis() != null) {
            gl.glVertex3dv(satelliteTrajectory.getApoapsis().getPosition().asArray(), 0);
        }
        if (satelliteTrajectory.getPeriapsis() != null) {
            gl.glVertex3dv(satelliteTrajectory.getPeriapsis().getPosition().asArray(), 0);
        }
        gl.glEnd();

        super.draw(drawable);
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        SatelliteTrajectory satelliteTrajectory = (SatelliteTrajectory) satellite.getTrajectory();
        renderApsis(satelliteTrajectory.getApoapsis());
        renderApsis(satelliteTrajectory.getPeriapsis());
    }

    protected void renderApsis(Apsis apsis) {
        if (apsis == null) {
            return;
        }
        Point size = getTextSize(apsis.getName());
        ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(apsis);
        if (viewCoordinates.isVisible()) {
            drawText(apsis.getName(), viewCoordinates.getPoint().getX() - size.getX() / 2.0, viewCoordinates.getPoint().getY() - 16);
        }
    }
}
