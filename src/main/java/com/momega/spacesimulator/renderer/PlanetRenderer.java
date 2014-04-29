package com.momega.spacesimulator.renderer;

import com.jogamp.common.util.IOUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Planet;

import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import java.io.IOException;
import java.io.InputStream;

import static javax.media.opengl.GL.*;

/**
 * The class renders the planet. It holds the {#link Planet} instance and contains logic for rendering.
 * Created by martin on 4/19/14.
 */
public class PlanetRenderer extends DynamicalPointRenderer {

    private final Planet planet;
    private Texture texture;
    private int listIndex;

    public PlanetRenderer(Planet planet, Camera camera) {
        super(planet, camera);
        this.planet = planet;
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

    public void init(GL2 gl) {
        super.init(gl);

        this.listIndex = gl.glGenLists(1);
        if (this.listIndex==0) {
            throw new IllegalStateException("gl list not created");
        }
        loadTexture(gl);
        gl.glNewList(this.listIndex, GL2.GL_COMPILE);
        prepareDraw(gl);
        gl.glEndList();
    }

    private void prepareDraw(GL2 gl) {
        GLU glu = new GLU();

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
        gl.glRotated(planet.getAxialTilt(), planet.getObject().getU().x, planet.getObject().getU().y, planet.getObject().getU().z);
        gl.glRotated(planet.getFi(), planet.getObject().getV().x, planet.getObject().getV().y, planet.getObject().getV().z);
        gl.glCallList(this.listIndex);
        gl.glPopMatrix();

        super.draw(gl);
    }
}
