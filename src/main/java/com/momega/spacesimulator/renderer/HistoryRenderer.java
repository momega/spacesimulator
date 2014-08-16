package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.HistoryPoint;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * History renderer display past trajectory of the spacecraft
 * Created by martin on 6/27/14.
 */
public class HistoryRenderer extends AbstractRenderer {

    private final Spacecraft spacecraft;

    public HistoryRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }
    
    @Override
    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glColor3dv(spacecraft.getHistoryTrajectory().getColor(), 0);
        gl.glBegin(GL.GL_LINE_STRIP);
        for (HistoryPoint hp : spacecraft.getHistoryTrajectory().getHistoryPoints()) {
            gl.glVertex3dv(hp.getPosition().asArray(), 0);
        }
        gl.glEnd();
        gl.glPopMatrix();
    }
}
