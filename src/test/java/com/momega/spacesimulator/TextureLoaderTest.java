package com.momega.spacesimulator;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static javax.media.opengl.GL2.*;

public class TextureLoaderTest implements GLEventListener {

    GLUT glut;
    GLU glu;

    private void makeRGBTexture(GL2 gl, GLU glu, BufferedImage img) {

        ByteBuffer dest = null;

        switch (img.getType()) {
            case BufferedImage.TYPE_3BYTE_BGR:
            case BufferedImage.TYPE_CUSTOM: {
                byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
                dest = ByteBuffer.allocateDirect(data.length);
                dest.order(ByteOrder.nativeOrder());
                dest.put(data, 0, data.length);
                break;
            }
            case BufferedImage.TYPE_INT_ARGB: {

                System.out.println("int rgb");
                int[] data = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
                dest = ByteBuffer.allocateDirect(data.length * 4);
                dest.order(ByteOrder.nativeOrder());
                dest.asIntBuffer().put(data, 0, data.length);
                break;
            }
            default:
                throw new RuntimeException("Unsupported image type " + img.getType());
        }


        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL_RGBA, img.getWidth(), img.getHeight(), 0, GL_BGRA, GL.GL_UNSIGNED_BYTE, dest);
    }

    int texture;

    public void init(GLAutoDrawable drawable) {

        glut = new GLUT();
        glu = new GLU();


        GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(0.17f, 0.65f, 0.92f, 0.0f); //sky color background
        //    gl.glClearColor(0.0f, 0.0f, 0.1f, 0.0f); //black background
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_TEXTURE_2D);

        final int[] tmp = new int[1];
        gl.glGenTextures(1, IntBuffer.wrap(tmp));

        texture = tmp[0];

        System.out.println("texture: "+texture);

        gl.glBindTexture(GL_TEXTURE_2D, texture);


        makeRGBTexture(gl, glu, getImage());

        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        //return tmp[0];
    }

    public BufferedImage getImage(){

        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graph = img.createGraphics();
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graph.scale(1, -1);
        graph.translate(0, -img.getHeight()+1);

        graph.setColor(Color.red);
        graph.drawOval(0, 0, img.getWidth()-1, img.getHeight());
        graph.drawLine(0, 0, img.getWidth()-5, img.getHeight());

        graph.setColor(Color.BLACK);
        graph.drawString("string", 10, 10);

        graph.dispose();

        // img = new AffineTransform().

        return img;
    }


    public void dispose(GLAutoDrawable drawable) {
    }
    private int angleX = 0;
    private int angleY = 0;

    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(1, 1, 0, 1);

        gl.glClear(GL_COLOR_BUFFER_BIT);
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        gl.glTranslated(0, 0, -2);

        gl.glColor3d(0, 0, 0);
        gl.glBegin(GL_LINES);

        gl.glVertex2d(0, 0);
        gl.glVertex2d(width, height);

        gl.glEnd();


        gl.glBindTexture(GL.GL_TEXTURE_2D, texture);

        gl.glEnable(GL_TEXTURE_2D);

        gl.glColor3d(1, 1, 1);

        gl.glBegin(GL_QUADS);
        {
            gl.glTexCoord2d(0, 0);
            gl.glVertex2d(0, 0);

            gl.glTexCoord2d(0, 1);
            gl.glVertex2d(0, 64);

            gl.glTexCoord2d(1, 1);
            gl.glVertex2d(64, 64);

            gl.glTexCoord2d(1, 0);
            gl.glVertex2d(64, 0);
        }
        gl.glEnd();

        gl.glDisable(GL_TEXTURE_2D);

    }
    private int width, height;

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        this.width = width;
        this.height = height;

        GL2 gl = drawable.getGL().getGL2();

        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // gl.glFrustum(-50, 50, -50, 50, 50, 150);

        //gl.glOrtho(height, height, width, y, height, angleX);

        gl.glOrtho(0, width, 0, height, 1, 3);

    }

    public static void main(String[] args) {

        TextureLoaderTest scheme = new TextureLoaderTest();
        GLCanvas canvas = new GLCanvas();

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();

        canvas.addGLEventListener(scheme);
        //canvas.addMouseListener(scheme);
        //canvas.addMouseMotionListener(scheme);

        JFrame frame = new JFrame("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.getContentPane().add(canvas);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }
}