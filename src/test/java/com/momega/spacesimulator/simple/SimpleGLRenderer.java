package com.momega.spacesimulator.simple;

import com.jogamp.opengl.util.texture.Texture;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.AbstractGLRenderer;
import com.momega.spacesimulator.opengl.GLLineUtils;
import com.momega.spacesimulator.opengl.GLUtils;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

/**
 * Anti-aliasing lines  
 * Created by martin on 7/1/14.
 * 
 * @see http://artgrammer.blogspot.com/2011/05/drawing-nearly-perfect-2d-line-segments.html
 * @see http://www.codeproject.com/KB/openGL/gllinedraw.aspx
 */
public class SimpleGLRenderer extends AbstractGLRenderer {

    private GLU glu;
    private Texture texture;

    @Override
    protected void computeScene() {

    }

    @Override
    protected void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
//        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
        //gl.glLoadIdentity();  // reset the model-view matrix
//
        // ----- Your OpenGL rendering code here (Render a white triangle for testing) -----
        gl.glTranslatef(0.0f, 0.0f, 0f); // translate into the screen
//        gl.glBegin(GL2.GL_TRIANGLE_STRIP); // draw using triangles
//        gl.glColor3f( 1,0,0);
//        gl.glVertex3f(0.0f, 1.0f, 0.0f);
//        gl.glColor3f( 0,1,0);
//        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
//        gl.glColor3f( 0,0,1);
//        gl.glVertex3f(1.0f, -1.0f, 0.0f);
//        gl.glEnd();
        
        gl.glColor3f( 1,0,0);
        gl.glLineWidth(5.0f);
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3f(-30.0f, 10.0f, 0.0f);
        gl.glVertex3f(30.0f, -10.0f, 0.0f);
        gl.glEnd();
        
        GLUtils.drawPoint(gl, 5, new double[] {1d,1d,1d}, Vector3d.ZERO);
        
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
        
        GLLineUtils.line(gl, -30.0, 20.0, 30.0, 0, 1, 1, 0, 0, 0, 0, 0, false);
        //GLLineUtils.line(gl, -30.0, 30.0, 30.0, -30.0, 1, 1, 0, 0, 0, 0, 0, false);
        //GLLineUtils.hair_line(gl, -3.0, -3.0, 3.0, 3.0, false);

        //gl.glTranslatef(50.0f, 50.0f, 0f); // translate into the screen
        
        int num_segments = 2800;
        double DEG2RAD = 2.0 * Math.PI / num_segments;
        double a = 60;
        double b = 40;
        
        double x = Math.cos(0) * a;
        double y = Math.sin(0) * b;
        for (int i=1; i<=num_segments ; i++) {
            double degInRad = DEG2RAD * i;
            double x1 = Math.cos(degInRad) * a; 
            double y1 = Math.sin(degInRad) * b;
            
            GLLineUtils.line(gl, x1, y1, x, y, 1, 1, 0, 0, 0, 0, 0, false);
            x = x1; y = y1;
        }
        
        
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
    	
    	gl.glBegin(GL2.GL_TRIANGLE_STRIP);
    	gl.glColor3f( 0,0,0);
    	gl.glVertex3f( 50,27,0);
    	gl.glVertex3f( 100,30,0);
    	gl.glColor3f( 0,0,1);
    	gl.glVertex3f( 54,27,0);
    	gl.glVertex3f( 104,30,0);
    	gl.glColor3f( 0,0,0);
    	gl.glVertex3f( 58,27,0);
    	gl.glVertex3f( 108,30,0);
    	gl.glEnd();

    	
//        // here start ortho and bitmap rendering
//        gl.glMatrixMode(GL2.GL_PROJECTION);
//        gl.glPushMatrix();
//        gl.glOrtho(0, drawable.getWidth(), 0, drawable.getHeight(), 0, 1);
//        //gl.glOrtho(0, 5, 0, 20, 0, 1);
//        //glu.gluPerspective(45, (double)drawable.getHeight() / (double)drawable.getWidth(), 1, 10);
//        //glu.gluLookAt(0, 0, 10, 0, 0, 0, 0, 1, 0);
//
//        gl.glMatrixMode(GL2.GL_MODELVIEW);
//        gl.glPushMatrix();
//        gl.glLoadIdentity();
//
//        // No depth buffer writes for background.
//        gl.glDepthMask( true );
//
//        texture.enable(gl);
//        texture.bind(gl);
//        gl.glNormal3d(0.0, 0.0, 1.0);
//        gl.glBegin( GL2.GL_QUADS ); {
//            gl.glTexCoord2d(0, 0); gl.glVertex2f(0, 0);
//            gl.glTexCoord2d(0, 1); gl.glVertex2f(0, 100);
//            gl.glTexCoord2d(1, 1); gl.glVertex2f(100, 100);
//            gl.glTexCoord2d(1, 0); gl.glVertex2f(100, 0);
//        } gl.glEnd();
//        texture.disable(gl);
//
//        gl.glDepthMask( true );
//
//        gl.glPopMatrix();
//        gl.glMatrixMode(GL2.GL_PROJECTION);
//        gl.glPopMatrix();
//        gl.glMatrixMode(GL2.GL_MODELVIEW);
    	
    	
    }

    @Override
    public void setCamera() {
        glu.gluLookAt(0, 0, 200, 0, 0, 0, 0, 1, 0);
    }

    @Override
    protected void init(GL2 gl) {
        glu = new GLU();
        texture = GLUtils.loadTexture(gl, getClass(), "earthmap1k.jpg");
    }

    @Override
    protected void setPerspective(GL2 gl, double aspect) {
        glu.gluPerspective(45, aspect, 1, 1000);
    }

}
