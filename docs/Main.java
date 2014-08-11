package com.momega.spacesimulator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import javax.media.opengl.GL;
import javax.media.opengl.GLProfile;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.glu.GLU;

public class Main {

    public static void main(String [] args) {
        GLProfile.initSingleton( );

        GLProfile glprofile = GLProfile.get( GLProfile.GL2 );

        Display display = new Display();
        Shell shell = new Shell( display );
        shell.setLayout( new FillLayout() );
        Composite composite = new Composite( shell, SWT.NONE );
        composite.setLayout( new FillLayout() );

        GLData gldata = new GLData();
        gldata.doubleBuffer = true;
        // need SWT.NO_BACKGROUND to prevent SWT from clearing the window
        // at the wrong times (we use glClear for this instead)
        final GLCanvas glcanvas = new GLCanvas( composite, SWT.NO_BACKGROUND, gldata );
        glcanvas.setCurrent();
        final GLContext glcontext = GLDrawableFactory.getFactory( glprofile ).createExternalGLContext();

        // fix the viewport when the user resizes the window
        glcanvas.addListener( SWT.Resize, new Listener() {
            public void handleEvent(Event event) {
                setup( glcanvas, glcontext );
            }
        });

        // draw the triangle when the OS tells us that any part of the window needs drawing
        glcanvas.addPaintListener( new PaintListener() {
            public void paintControl( PaintEvent paintevent ) {
                render( glcanvas, glcontext );
            }
        });

        shell.setText( "OneTriangle" );
        shell.setSize( 640, 480 );
        shell.open();

        while( !shell.isDisposed() ) {
            if( !display.readAndDispatch() )
                display.sleep();
        }

        glcanvas.dispose();
        display.dispose();
    }

    private static void setup( GLCanvas glcanvas, GLContext glcontext ) {
        Rectangle rectangle = glcanvas.getClientArea();

        glcanvas.setCurrent();
        glcontext.makeCurrent();

        GL2 gl = glcontext.getGL().getGL2();
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();

        // coordinate system origin at lower left with width and height same as the window
        GLU glu = new GLU();
        glu.gluOrtho2D( 0.0f, rectangle.width, 0.0f, rectangle.height );

        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();

        gl.glViewport( 0, 0, rectangle.width, rectangle.height );
        glcontext.release();
    }

    private static void render( GLCanvas glcanvas, GLContext glcontext ) {
        Rectangle rectangle = glcanvas.getClientArea();

        glcanvas.setCurrent();
        glcontext.makeCurrent();

        GL2 gl = glcontext.getGL().getGL2();
        gl.glClear( GL.GL_COLOR_BUFFER_BIT );

        // draw a triangle filling the window
        gl.glLoadIdentity();
        gl.glBegin( GL.GL_TRIANGLES );
        gl.glColor3f( 1, 0, 0 );
        gl.glVertex2f( 0, 0 );
        gl.glColor3f( 0, 1, 0 );
        gl.glVertex2f( rectangle.width, 0 );
        gl.glColor3f( 0, 0, 1 );
        gl.glVertex2f( rectangle.width / 2, rectangle.height );
        gl.glEnd();

        glcanvas.swapBuffers();
        glcontext.release();
    }
}