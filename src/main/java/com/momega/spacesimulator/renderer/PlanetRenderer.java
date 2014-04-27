package com.momega.spacesimulator.renderer;

import com.jogamp.common.util.IOUtil;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.Vector3d;

import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import static javax.media.opengl.GL.*;

/**
 * The class renders the planet. It holds the {#link Planet} instance and contains logic for rendering.
 * Created by martin on 4/19/14.
 */
public class PlanetRenderer {

    private final Planet planet;
    private final Camera camera;
    private Texture texture;
    private int listIndex;
    private TrajectoryRenderer trajectoryRenderer;
    private TextRenderer textRenderer;

    public PlanetRenderer(Planet planet, Camera camera) {
        this.planet = planet;
        this.camera = camera;
        this.trajectoryRenderer = TrajectoryRenderer.createInstance(planet);
    }

    public void loadTexture(GL2 gl) {
        this.texture = loadTexture(gl, planet.getTextureFileName());
    }

    private Texture loadTexture(GL2 gl, String fileName) {

        InputStream stream = null;
        try {
            stream = getClass().getResourceAsStream(fileName);
            TextureData data = TextureIO.newTextureData(GLProfile.getDefault(), stream, true, "jpg");
            Texture result = TextureIO.newTexture(data);
            result.setTexParameteri(gl, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            result.setTexParameteri(gl, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            return result;
        }
        catch (IOException exc) {
            IOUtil.close(stream, true);
        }

        return null;
    }

    public void dispose(GL2 gl) {
        gl.glDeleteLists(this.listIndex, 1);
        if (texture != null) {
            texture.destroy(gl);
        }
    }

    public void init(GL2 gl, GLU glu) {
        this.listIndex = gl.glGenLists(1);
        if (this.listIndex==0) {
            throw new IllegalStateException("gl list not created");
        }
        loadTexture(gl);
        gl.glNewList(this.listIndex, GL2.GL_COMPILE);
        prepareDraw(gl, glu);
        gl.glEndList();

        this.textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12));
    }

    private void prepareDraw(GL2 gl, GLU glu) {
        texture.enable(gl);
        texture.bind(gl);
        GLUquadric quadric = glu.gluNewQuadric();
        gl.glColor3d(1, 1, 1);
        glu.gluQuadricTexture(quadric, true);
        glu.gluQuadricNormals(quadric, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(quadric, GLU.GLU_OUTSIDE);
        glu.gluSphere(quadric, planet.getRadius(), 64, 64);
        glu.gluDeleteQuadric(quadric);
        texture.disable(gl);

        gl.glLineWidth(2.5f);
        gl.glBegin(GL_LINES);
        gl.glVertex3d(0, 0, planet.getRadius() * 2);
        gl.glVertex3d(0, 0, -planet.getRadius() * 2);
        gl.glEnd();
    }

    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslated(planet.getPosition().x, planet.getPosition().y, planet.getPosition().z);
        gl.glRotated(planet.getFi() * 180 / Math.PI, planet.getV().x, planet.getV().y, planet.getV().z);
        gl.glRotated(planet.getAxialTilt(), planet.getU().x, planet.getU().y, planet.getU().z);
        gl.glCallList(this.listIndex);

        gl.glPopMatrix();

        double modelview[] = new double[16];
        double projection[] = new double[16];
        double[] my2DPoint = new double[4]; // will contain 2d window coordinates when done
        int viewport[] = new int[4];
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelview, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projection, 0 );
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0 );

        Vector3d viewVector = camera.getN();
        Vector3d diffVector = camera.getPosition().clone().negate().add(planet.getPosition());

        if (viewVector.dot(diffVector) > 0) {  // object is in front of the camera
            GLU glu = new GLU();
            glu.gluProject(planet.getPosition().x, planet.getPosition().y, planet.getPosition().z, modelview, 0, projection, 0, viewport, 0, my2DPoint, 0);

            textRenderer.beginRendering(viewport[2], viewport[3]);
            textRenderer.setColor(1, 1, 1, 1);
            textRenderer.draw(planet.getName(), (int) my2DPoint[0] + 5, (int) my2DPoint[1] + 5);
            textRenderer.endRendering();
        }

        trajectoryRenderer.draw(gl);
    }
}
