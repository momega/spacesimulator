package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.ArtificialBody;
import com.momega.spacesimulator.model.HistoryPoint;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * History renderer display past trajectory of the artificialBody
 * Created by martin on 6/27/14.
 */
public class HistoryRenderer extends AbstractRenderer {

    private final ArtificialBody artificialBody;

    public HistoryRenderer(ArtificialBody artificialBody) {
        this.artificialBody = artificialBody;
    }
    
    @Override
    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glColor3dv(artificialBody.getHistoryTrajectory().getColor(), 0);
        gl.glBegin(GL.GL_LINE_STRIP);
        for (HistoryPoint hp : artificialBody.getHistoryTrajectory().getHistoryPoints()) {
            gl.glVertex3dv(hp.getPosition().asArray(), 0);
        }
        gl.glEnd();
        gl.glPopMatrix();
    }
}
