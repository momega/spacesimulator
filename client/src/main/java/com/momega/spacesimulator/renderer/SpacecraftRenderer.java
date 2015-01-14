package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;

/**
 * Simple renderer of the spacecraft
 * Created by martin on 5/6/14.
 */
public class SpacecraftRenderer extends AbstractRenderer {

    private final double size = 1d;
    
    private final double axisSize = 1E6;

    private final Spacecraft spacecraft;

    public SpacecraftRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    public void draw(GLAutoDrawable drawable) {
        if (RendererModel.getInstance().isVisibleOnScreen(spacecraft)) {
            GL2 gl = drawable.getGL().getGL2();
            
            gl.glPushMatrix();

        	if (Preferences.getInstance().isDrawSpacecraftAxisActivated()) {
    	        Vector3d n = spacecraft.getPosition().scaleAdd(axisSize, spacecraft.getOrientation().getN());
    	        Vector3d z = spacecraft.getPosition().scaleAdd(axisSize, spacecraft.getOrientation().getV());
    	        Vector3d u = spacecraft.getPosition().scaleAdd(axisSize, spacecraft.getOrientation().getU());
    	        gl.glColor3dv(spacecraft.getTrajectory().getColor(), 0);
    	        gl.glLineWidth(1f);
    	        gl.glBegin(GL2.GL_LINES);
    	        gl.glVertex3dv(n.asArray(), 0);
    	        gl.glVertex3dv(spacecraft.getPosition().asArray(), 0);
    	        gl.glEnd();
    	
    	        gl.glColor3dv(new double[] {1,1,1}, 0);
    	        gl.glBegin(GL2.GL_LINE_STRIP);
    	        gl.glVertex3dv(u.asArray(), 0);
    	        gl.glVertex3dv(spacecraft.getPosition().asArray(), 0);
    	        gl.glVertex3dv(z.asArray(), 0);
    	        gl.glEnd();
        	}
        	
        	if (spacecraft.getThrust() != null) {
    	        Vector3d t = spacecraft.getPosition().scaleAdd(axisSize, spacecraft.getThrust());
    	        GLUtils.drawMultiLine(gl, 2.5d, new double[]{1, 0, 0}, new Vector3d[]{spacecraft.getPosition(), t});
	        }
            
            gl.glPopMatrix();
        }

    }
}
