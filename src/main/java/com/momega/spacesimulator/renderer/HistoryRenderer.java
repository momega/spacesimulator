package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.Satellite;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * History renderer display past trajectory of the satellite
 * Created by martin on 6/27/14.
 */
public class HistoryRenderer extends AbstractRenderer {

    private final Satellite satellite;

    public HistoryRenderer(Satellite satellite) {
        this.satellite = satellite;
    }
    
    @Override
    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glColor3dv(satellite.getHistoryTrajectory().getColor(), 0);
        gl.glBegin(GL.GL_LINE_STRIP);
        for (HistoryPoint hp : satellite.getHistoryTrajectory().getHistoryPoints()) {
            gl.glVertex3dv(hp.getPosition().asArray(), 0);
        }
        gl.glEnd();
        gl.glPopMatrix();
    }
}
