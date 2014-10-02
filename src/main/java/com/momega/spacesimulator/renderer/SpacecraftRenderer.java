package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;

/**
 * Created by martin on 5/6/14.
 */
public class SpacecraftRenderer extends AbstractRenderer {

    private final double size = 1d;
    
    private final double axisSize = 1E6;

    private final Spacecraft spacecraft;
	private boolean drawAxis;

    public SpacecraftRenderer(Spacecraft spacecraft, boolean drawAxis) {
        this.spacecraft = spacecraft;
		this.drawAxis = drawAxis;
    }

    public void draw(GLAutoDrawable drawable) {
        if (RendererModel.getInstance().isVisibleOnScreen(spacecraft)) {
            GL2 gl = drawable.getGL().getGL2();
            
            gl.glPushMatrix();
            GLUtils.drawPoint(gl, 8, spacecraft.getTrajectory().getColor(), spacecraft);

        	if (drawAxis) {
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
    	        GLUtils.drawMultiLine(gl, 2.5d, new double[] {1,0,0}, new Vector3d[] {spacecraft.getPosition(), t});
	        }
            
            gl.glPopMatrix();
            
//            if (viewCoordinates.getRadius()>0.001) {
//                GLU glu = new GLU();
//
//                gl.glPushMatrix();
//                GLUtils.translate(gl, spacecraft.getCartesianState().getPosition());
//                gl.glRotatef(45, 0, 0, -1);
//                gl.glRotated(45d, 0, 1, 0);
//
//                //TODO: there is no structure of the spacecraft yet
//                // Draw spacecraft body.
//                gl.glColor3d(0.4d, 0.4d, 0.4d);
//                final double cylinderRadius = 1d * size;
//                final double cylinderHeight = 3d * size;
//                GLUquadric body = glu.gluNewQuadric();
//                glu.gluQuadricTexture(body, false);
//                glu.gluQuadricDrawStyle(body, GLU.GLU_FILL);
//                glu.gluQuadricNormals(body, GLU.GLU_FLAT);
//                glu.gluQuadricOrientation(body, GLU.GLU_OUTSIDE);
//                gl.glTranslated(0, 0, -cylinderHeight / 2);
//                glu.gluDisk(body, 0, cylinderRadius, 64, 2);
//                glu.gluCylinder(body, cylinderRadius, cylinderRadius, cylinderHeight, 64, 64);
//                gl.glTranslated(0, 0, cylinderHeight);
//                glu.gluDisk(body, 0, cylinderRadius, 64, 2);
//                glu.gluDeleteQuadric(body);
//                gl.glTranslated(0, 0, -cylinderHeight / 2);
//
//                gl.glScaled(6d * size, 0.7d * size, 0.1d * size);
//                gl.glColor3d(0.4d, 0d, 0.8);
//                gl.glBegin(GL2.GL_QUADS);
//                final float[] frontUL = {-1.0f, -1.0f, 1.0f};
//                final float[] frontUR = {1.0f, -1.0f, 1.0f};
//                final float[] frontLR = {1.0f, 1.0f, 1.0f};
//                final float[] frontLL = {-1.0f, 1.0f, 1.0f};
//                final float[] backUL = {-1.0f, -1.0f, -1.0f};
//                final float[] backLL = {-1.0f, 1.0f, -1.0f};
//                final float[] backLR = {1.0f, 1.0f, -1.0f};
//                final float[] backUR = {1.0f, -1.0f, -1.0f};
//                // Front Face.
//                gl.glNormal3f(0.0f, 0.0f, 1.0f);
//                gl.glTexCoord2f(0.0f, 0.0f);
//                gl.glVertex3fv(frontUR, 0);
//                gl.glTexCoord2f(1.0f, 0.0f);
//                gl.glVertex3fv(frontUL, 0);
//                gl.glTexCoord2f(1.0f, 1.0f);
//                gl.glVertex3fv(frontLL, 0);
//                gl.glTexCoord2f(0.0f, 1.0f);
//                gl.glVertex3fv(frontLR, 0);
//                // Back Face.
//                gl.glNormal3f(0.0f, 0.0f, -1.0f);
//                gl.glTexCoord2f(0.0f, 0.0f);
//                gl.glVertex3fv(backUL, 0);
//                gl.glTexCoord2f(1.0f, 0.0f);
//                gl.glVertex3fv(backUR, 0);
//                gl.glTexCoord2f(1.0f, 1.0f);
//                gl.glVertex3fv(backLR, 0);
//                gl.glTexCoord2f(0.0f, 1.0f);
//                gl.glVertex3fv(backLL, 0);
//                gl.glEnd();
//
//                gl.glPopMatrix();
//            }
//            else {
               
//            }
        }

    }
}
