package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.opengl.GLUtils;

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
        GLUtils.drawMultiLine(gl, 1, spacecraft.getHistoryTrajectory().getColor(), 
        		spacecraft.getHistoryTrajectory().getHistoryPoints());
        gl.glPopMatrix();
    }
}
