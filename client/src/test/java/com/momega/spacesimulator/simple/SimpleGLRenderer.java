package com.momega.spacesimulator.simple;

import java.nio.FloatBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import com.momega.spacesimulator.opengl.AbstractGLRenderer;

/**
 */
public class SimpleGLRenderer extends AbstractGLRenderer {

    private GLU glu;
    
    float[] ctrlPoints = {
            -4.0f, -4.0f, 0.0f, 
            -3.0f, 0.0f, 0.0f,
            2.0f, 1.0f, 0.0f, 
            4.0f, 4.0f, 0.0f
    };

    @Override
    protected void computeScene() {

    }

    @Override
    protected void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glEnable(GL2.GL_MAP1_VERTEX_3);
        gl.glBegin(GL2.GL_LINE_STRIP);
        for (int i = 0; i <= 30; i++) {
            gl.glEvalCoord1f(i / 30.0f);
        }
        gl.glEnd();
        gl.glDisable(GL2.GL_MAP1_VERTEX_3);
        
        gl.glPointSize(5.0f);
        gl.glColor3f(1.0f, 1.0f, 0.0f);
        gl.glBegin(GL2.GL_POINTS);
        for (int i = 0; i < 4; i++) {
            gl.glVertex3f(ctrlPoints[i * 3], ctrlPoints[i * 3 + 1], ctrlPoints[i * 3 + 2]);
        }
        gl.glEnd();
    }

    @Override
    public void setCamera() {
        glu.gluLookAt(0, 0, 20, 0, 0, 0, 0, 1, 0);
    }

    @Override
    protected void setup(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        
        FloatBuffer ctrlPointBuffer = FloatBuffer.allocate(12);
        for (int i = 0; i < ctrlPoints.length; i++) {
            ctrlPointBuffer.put(ctrlPoints[i]);
        }
        ctrlPointBuffer.rewind();
        gl.glMap1f(GL2.GL_MAP1_VERTEX_3, 0.0f, 1.0f, 3, 4, ctrlPointBuffer);
        gl.glEnable(GL2.GL_MAP1_VERTEX_3);
    }

    @Override
    protected void setPerspective(GL2 gl, double aspect) {
        glu.gluPerspective(45, aspect, 1, 1000);
    }

}
