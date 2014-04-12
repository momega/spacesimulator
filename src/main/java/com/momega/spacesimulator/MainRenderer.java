package com.momega.spacesimulator;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
// GL2 constants


import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class MainRenderer implements GLEventListener {
    private GLU glu;  // for the GL Utility
    private GLUT glut;

    private static final Logger logger = LoggerFactory.getLogger(MainRenderer.class);
    private TextRenderer textRenderer;

    private Camera camera;
    private Vector3d lightPosition = new Vector3d();

    private boolean specular = true;
    private boolean diffuse = false;
    private boolean emmision = false;
    private boolean fog = false;

    private Texture marsTexture;

    public MainRenderer() {
    }

    /**
     * Called back immediately after the OpenGL context is initialized. Can be used
     * to perform one-time initialization. Run only once.
     */
    public void init(GLAutoDrawable drawable) {
        logger.info("Renderer initializer");

        GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
        drawable.setGL(new DebugGL2(gl));
        glu = new GLU();
        glut = new GLUT();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
//	      gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

        textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12));

        // Set up and enable a z-buffer.
        gl.glClearDepth(1.0);
        //gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT0);
        gl.glEnable(GL_COLOR_MATERIAL);

        float[] blackAmbientLight = {0.0f, 0.0f, 0.0f};
        float[] whiteSpecularLight = {1f, 1f, 1f, 1f};
        float[] whiteDiffuseLight = {1f, 1f, 1f};

        gl.glLightfv(GL_LIGHT0, GL_SPECULAR, whiteSpecularLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, blackAmbientLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, whiteDiffuseLight, 0);

        gl.glShadeModel(GL_SMOOTH);

        // Set up and enable back-face culling.
        //gl.glFrontFace(GL.GL_CCW);
        //gl.glEnable(GL.GL_CULL_FACE);

        gl.glEnable(GL_FOG);
        gl.glFogi(GL_FOG_MODE, GL_EXP);
        gl.glFogfv(GL_FOG_COLOR, new float[] {0.3f, 0.3f, 0.3f, 1f}, 0);
        gl.glHint(GL_FOG_HINT, GL_NICEST);
        gl.glFogf(GL_FOG_START, 1);
        gl.glFogf(GL_FOG_END, 10);
        gl.glFogf(GL_FOG_DENSITY, 0.03f);

        try {
            InputStream stream = getClass().getResourceAsStream("mars_1k_color.jpg");
            TextureData data = TextureIO.newTextureData(GLProfile.getDefault(), stream, false, "jpg");
            this.marsTexture = TextureIO.newTexture(data);

            marsTexture.setTexParameteri(gl, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
            marsTexture.setTexParameterf(gl, GL_TEXTURE_MAG_FILTER, GL_LINEAR );

            marsTexture.enable(gl);
            marsTexture.bind(gl);
        }
        catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }

        reset();
        logger.info("renderer initializaed");
    }

    /**
     * Call-back handler for window re-size event. Also called when the drawable is
     * first set to visible.
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        logger.info("reshape called {}x{}", width, height);
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

        if (height == 0) {
            height = 1;   // prevent divide by zero
        }
        float aspect = (float) width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45, aspect, 1, 1000);
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset

        logger.info("reshape finished");
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        render(drawable);
    }

    /**
     * Called back by the animator to perform rendering.
     */
    private void render(GLAutoDrawable drawable) {
        // logger.info("render called");
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

        camera.setView(gl, glu);

        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.toFloat(), 0);

        if (fog) {
            gl.glEnable(GL_FOG);
        } else {
            gl.glDisable(GL_FOG);
        }

        gl.glLineWidth(2.5f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3f(100.0f, 0.0f, 0.0f);
        gl.glVertex3f(-100.0f, 0.0f, 0.0f);
        gl.glEnd();

        gl.glLineWidth(2.5f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3f(100.0f, 100.0f, 0.0f);
        gl.glVertex3f(-100.0f, 100.0f, 0.0f);
        gl.glEnd();

        gl.glLineWidth(2.5f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3f(100.0f, -100.0f, 0.0f);
        gl.glVertex3f(-100.0f, -100.0f, 0.0f);
        gl.glEnd();

        gl.glLineWidth(2.5f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3f(100.0f, 100.0f, 0.0f);
        gl.glVertex3f(100.0f, -100.0f, 0.0f);
        gl.glEnd();

        gl.glLineWidth(2.5f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3f(-100.0f, 100.0f, 0.0f);
        gl.glVertex3f(-100.0f, -100.0f, 0.0f);
        gl.glEnd();

        gl.glLineWidth(2.5f);
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3f(00.0f, 100.0f, 0.0f);
        gl.glVertex3f(00.0f, -100.0f, 0.0f);
        gl.glEnd();

        gl.glLineWidth(2.5f);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3f(00.0f, 0.0f, 100.0f);
        gl.glVertex3f(00.0f, 0.0f, -100.0f);
        gl.glEnd();

        gl.glPushMatrix();
        GLUquadric light = glu.gluNewQuadric();
        gl.glColor3f(1f, 1f, 1f);
        gl.glTranslatef((float) lightPosition.x, (float) lightPosition.y, (float) lightPosition.z);
        glu.gluSphere(light, 1f, 32, 32);
        glu.gluDeleteQuadric(light);
        gl.glPopMatrix();

        // ------------ Render Objects ----------------

        float[] white = new float[] {1.0f, 1.0f, 1.0f};
        float[] redDiffuse = new float[] {1.0f, 1.0f, 1.0f};
        float[] greenEmmision = new float[] {0.0f, 0.1f, 0.0f};
        float[] blank = new float[] {0.0f, 0.0f, 0.0f};
        float[] mShininess = new float[] {128.0f};

        if (specular) {
            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, white, 0);
            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SHININESS, mShininess, 0);
        } else {
            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, blank, 0);
            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SHININESS, blank, 0);
        }

        if (diffuse) {
            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, redDiffuse, 0);
        } else {
            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, blank, 0);
        }

        if (emmision) {
            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_EMISSION, greenEmmision, 0);
        } else {
            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_EMISSION, blank, 0);
        }



        gl.glPushMatrix();
        GLUquadric earth = glu.gluNewQuadric();
        glu.gluQuadricTexture(earth, true);
        //gl.glColor3f(1f, 1f, 1f);
        gl.glTranslatef(30f, -50f, 0.0f);
        glu.gluSphere(earth, 20.0f, 64, 64);
        glu.gluDeleteQuadric(earth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        GLUquadric box = glu.gluNewQuadric();
        gl.glColor3f(0.0f, 0.1f, 0.5f);
        gl.glTranslatef(-50.0f, -90f, 0f);
        glut.glutSolidCube(30f);
        glu.gluDeleteQuadric(box);
        gl.glPopMatrix();

        gl.glPushMatrix();
        GLUquadric cylinder = glu.gluNewQuadric();
        gl.glColor3f(0.1f, 0.6f, 0.1f);
        gl.glTranslatef(50.0f, 60f, -20f);
        glut.glutSolidCylinder(15f, 30f, 24, 1);
        glu.gluDeleteQuadric(cylinder);
        gl.glPopMatrix();

        gl.glPushMatrix();
        GLUquadric jehlan = glu.gluNewQuadric();
        gl.glColor3f(0.9f, 0.2f, 0.1f);
        gl.glTranslatef(-60.0f, 40f, -20f);
        glut.glutSolidCone(25f, 35f, 40, 1);
        glu.gluDeleteQuadric(jehlan);
        gl.glPopMatrix();

        gl.glPushMatrix();
        GLUquadric torus = glu.gluNewQuadric();
        gl.glColor3f(0.9f, 0.9f, 0.0f);
        gl.glTranslatef(-50.0f, 100f, 10f);
        gl.glRotatef(90f, 0.0f, 1f, 0f);
        glut.glutSolidTorus(5.0f, 20.f, 24, 60);
        glu.gluDeleteQuadric(torus);
        gl.glPopMatrix();

        gl.glPushMatrix();
        GLUquadric dodec = glu.gluNewQuadric();
        gl.glColor3f(0.0f, 0.9f, 0.9f);
        gl.glTranslatef(40.0f, 0f, 0f);
        gl.glScalef(5.0f, 5.0f, 5.0f);
        glut.glutSolidDodecahedron();
        glu.gluDeleteQuadric(dodec);
        gl.glPopMatrix();

        textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
        // optionally set the color
        textRenderer.setColor(1f, 1f, 1f, 1f);
        textRenderer.draw("Position:" + camera.getPosition().toString(), 10, 40);
        textRenderer.draw("N:" + camera.getN().toString(), 10, 10);
        textRenderer.draw("U:" + camera.getU().toString(), 400, 40);
        textRenderer.draw("V:" + camera.getV().toString(), 400, 10);
        textRenderer.draw("Mat Specular:" + specular + " Diffuse:" + diffuse + " Emmision:" + emmision + " Fog:" + fog, 10, 70);
        textRenderer.draw("Light:" + lightPosition.toString(), 400, 70);
        textRenderer.endRendering();

        gl.glFlush();
    }

    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    public void dispose(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        marsTexture.destroy(gl);
        logger.info("renderer disposed");
    }

    public void stepPosition(float step) {
        camera.moveN(step);
    }

    public void stepAngleTheta(float step) {
        camera.rotate(camera.getU(), step);
    }

    public void stepAngleFi(float step) {
        camera.rotate(new Vector3d(0,0,1), step);
    }

    public void reset() {
        camera = new Camera(new Vector3d(0, 0, 0), new Vector3d(1, 1, 0), new Vector3d(0, 0, 1));
    }

    public void twist(float step) {
        camera.rotate(camera.getN(), step);
    }

    public void switchDiuffuse() {
        this.diffuse = !this.diffuse;
    }

    public void switchSpecular() {
        this.specular = !this.specular;
    }

    public void moveLight(float stepX, float stepY) {
       this.lightPosition.x += stepX;
       this.lightPosition.y += stepY;
    }

    public void switchEmmision() {
        this.emmision = !this.emmision;
    }

    public void switchFog() {
        this.fog = !this.fog;
    }
}
