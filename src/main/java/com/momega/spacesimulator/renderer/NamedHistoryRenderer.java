package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.Spacecraft;

/**
 * Created by martin on 8/24/14.
 */
public class NamedHistoryRenderer extends AbstractPositionProviderRenderer {

    private final Spacecraft spacecraft;

    public NamedHistoryRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    @Override
    protected void drawObjects(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPointSize(8);
        gl.glColor3dv(this.spacecraft.getHistoryTrajectory().getColor(), 0);

        gl.glBegin(GL2.GL_POINTS);
        for(HistoryPoint hp : spacecraft.getHistoryTrajectory().getNamedHistoryPoints()) {
            gl.glVertex3dv(hp.getPosition().asArray(), 0);
        }
        gl.glEnd();
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        for(HistoryPoint hp : spacecraft.getHistoryTrajectory().getNamedHistoryPoints()) {
            renderPositionProvider(hp);
        }
    }
}
