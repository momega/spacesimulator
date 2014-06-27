package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Satellite;
import com.momega.spacesimulator.model.Vector3d;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Created by martin on 6/27/14.
 */
public class HistoryRenderer extends AbstractRenderer {

    private final MovingObject movingObject;

    public HistoryRenderer(MovingObject movingObject) {
        this.movingObject = movingObject;        
    }
    
    @Override
    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glColor3dv(movingObject.getTrajectory().getTrajectoryColor(), 0);
        gl.glBegin(GL.GL_LINE_STRIP);
        for (Vector3d v : movingObject.getTrajectory().getHistory()) {
            gl.glVertex3dv(v.asArray(), 0);
        }
        gl.glEnd();
        gl.glPopMatrix();
    }
}
