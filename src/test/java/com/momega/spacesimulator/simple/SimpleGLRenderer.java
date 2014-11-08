package com.momega.spacesimulator.simple;

import java.nio.Buffer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.texture.Texture;
import com.momega.spacesimulator.opengl.AbstractGLRenderer;

/**
 */
public class SimpleGLRenderer extends AbstractGLRenderer {

    private GLU glu;
    private Texture texture;

    int imgHeight;
    int imgWidth;

    Buffer imgRGBA = null;

    @Override
    protected void computeScene() {

    }

    @Override
    protected void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
//        //gl.glLoadIdentity();  // reset the model-view matrix
////
//        // ----- Your OpenGL rendering code here (Render a white triangle for testing) -----
//        gl.glTranslatef(0.0f, 0.0f, 0f); // translate into the screen
////        gl.glBegin(GL2.GL_TRIANGLE_STRIP); // draw using triangles
////        gl.glColor3f( 1,0,0);
////        gl.glVertex3f(0.0f, 1.0f, 0.0f);
////        gl.glColor3f( 0,1,0);
////        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
////        gl.glColor3f( 0,0,1);
////        gl.glVertex3f(1.0f, -1.0f, 0.0f);
////        gl.glEnd();
//
//        gl.glColor3f( 1,0,0);
//        gl.glLineWidth(5.0f);
//        gl.glBegin(GL2.GL_LINES);
//        gl.glVertex3f(-30.0f, 10.0f, 0.0f);
//        gl.glVertex3f(30.0f, -10.0f, 0.0f);
//        gl.glEnd();
//
//        GLUtils.drawPoint(gl, 5, new double[] {1d,1d,1d}, Vector3d.ZERO);
//
//        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
//    	gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
//
//        GLLineUtils.line(gl, -30.0, 20.0, 30.0, 0, 1, 1, 0, 0, 0, 0, 0, false);
//        //GLLineUtils.line(gl, -30.0, 30.0, 30.0, -30.0, 1, 1, 0, 0, 0, 0, 0, false);
//        //GLLineUtils.hair_line(gl, -3.0, -3.0, 3.0, 3.0, false);
//
//        //gl.glTranslatef(50.0f, 50.0f, 0f); // translate into the screen
//
//        int num_segments = 2800;
//        double DEG2RAD = 2.0 * Math.PI / num_segments;
//        double a = 60;
//        double b = 40;
//
//        double x = Math.cos(0) * a;
//        double y = Math.sin(0) * b;
//        for (int i=1; i<=num_segments ; i++) {
//            double degInRad = DEG2RAD * i;
//            double x1 = Math.cos(degInRad) * a;
//            double y1 = Math.sin(degInRad) * b;
//
//            GLLineUtils.line(gl, x1, y1, x, y, 1, 1, 0, 0, 0, 0, 0, false);
//            x = x1; y = y1;
//        }
//
//
//        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
//    	gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
//
//    	gl.glBegin(GL2.GL_TRIANGLE_STRIP);
//    	gl.glColor3f( 0,0,0);
//    	gl.glVertex3f( 50,27,0);
//    	gl.glVertex3f( 100,30,0);
//    	gl.glColor3f( 0,0,1);
//    	gl.glVertex3f( 54,27,0);
//    	gl.glVertex3f( 104,30,0);
//    	gl.glColor3f( 0,0,0);
//    	gl.glVertex3f( 58,27,0);
//    	gl.glVertex3f( 108,30,0);
//    	gl.glEnd();


//        // here start ortho and bitmap rendering
//        gl.glMatrixMode(GL2.GL_PROJECTION);
//        gl.glPushMatrix();
//        gl.glOrtho(0, 1, 0, 1, 0, 1);
//
//        gl.glMatrixMode(GL2.GL_MODELVIEW);
//        gl.glPushMatrix();
//        gl.glLoadIdentity();
//
//        // No depth buffer writes for background.
//        //gl.glDepthMask( false );
//        //gl.glPushAttrib( GL2.GL_DEPTH_BUFFER_BIT );
//        //gl.glPushAttrib( GL2.GL_COLOR_BUFFER_BIT );
//        gl.glDisable(GL2.GL_DEPTH_TEST);
//
//        //texture.enable(gl);
//        //texture.bind(gl);
//        gl.glNormal3d(0.0, 0.0, 1.0);
//        gl.glBegin( GL2.GL_QUADS ); {
//            gl.glTexCoord2d(0, 0); gl.glVertex2f(0, 0);
//            gl.glTexCoord2d(0, 1); gl.glVertex2f(0, 1);
//            gl.glTexCoord2d(1, 1); gl.glVertex2f(1, 1);
//            gl.glTexCoord2d(1, 0); gl.glVertex2f(1, 0);
//        } gl.glEnd();
//        //texture.disable(gl);
//
//        gl.glEnable(GL2.GL_DEPTH_TEST);
//        //gl.glDepthMask( true );
//
//        gl.glPopMatrix();
//        gl.glMatrixMode(GL2.GL_PROJECTION);
//        gl.glPopMatrix();
//        gl.glMatrixMode(GL2.GL_MODELVIEW);


        gl.glPushAttrib(GL2.GL_DEPTH_BUFFER_BIT);
        gl.glPushAttrib(GL2.GL_COLOR_BUFFER_BIT);
        {

            gl.glDisable(GL2.GL_DEPTH_TEST);

            // enable alpha mask (import from gif sets alpha bits)
            gl.glEnable(GL2.GL_BLEND);
            gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

            // Draw a rectangle under part of image
            // to prove alpha works.
//            gl.glColor4f(.5f, 0.1f, 0.2f, .5f);
//            gl.glRecti(0, 0, 100, 330);
//            gl.glColor3f(0.0f, 0.0f, 0.0f);


            // Draw image as bytes.
            // gl.glRasterPos2i( 150, 100 );
            gl.glWindowPos2i(200, 200);
            gl.glPixelZoom(1f, 1f); // x-factor, y-factor
            gl.glDrawPixels(imgWidth, imgHeight,
                    GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE,
                    imgRGBA);

//            gl.glPixelZoom(-2.0f, 3.0f); // x-factor, y-factor
//            gl.glWindowPos2i(600, 300);
//            gl.glDrawPixels(imgWidth, imgHeight,
//                    gl.GL_RGBA, gl.GL_UNSIGNED_BYTE,
//                    Buffers.newDirectByteBuffer(imgRGBA));

// 	// Draw a rectangle under part of image
// 	// to prove alpha works.
// 	gl.glColor4f( .5f, 0.1f, 0.2f, .5f );
// 	gl.glRecti( 0, 0, 100, 330 );

            // Copy the Image: FrameBuf to FrameBuf
//            gl.glPixelZoom(1.0f, 1.0f); // x-factor, y-factor
//            gl.glWindowPos2i(500, 0);
//            gl.glCopyPixels(400, 300, 500, 600, GL2.GL_COLOR);

        }
        gl.glPopAttrib();
        gl.glPopAttrib();
    }



    @Override
    public void setCamera() {
        glu.gluLookAt(0, 0, 1000, 0, 0, 0, 0, 1, 0);
    }

    @Override
    protected void setup(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        //texture = GLUtils.loadTexture(gl, getClass(), );

        //imgRGBA = loadImage2( getClass(), "/images/letter_s_99524.jpg" );

//        try {
//            imgRGBA = loadImage("/images/Letter-P-icon.png");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void setPerspective(GL2 gl, double aspect) {
        glu.gluPerspective(45, aspect, 1, 1000);
    }

}
