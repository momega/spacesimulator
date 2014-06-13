package com.momega.spacesimulator.renderer;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
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

    private static final Logger logger = LoggerFactory.getLogger(PlanetRenderer.class);

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
            IOUtils.closeQuietly(stream);
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

    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();

        GLUtils.translate(gl, planet.getPosition());
        double axialTilt = Math.toDegrees(Vector3d.angleBetween(new Vector3d(0,0,1), planet.getOrientation().getV()));
        gl.glRotated(axialTilt, 1, 0, 0);
        double phi = Math.toDegrees(Vector3d.angleBetween(new Vector3d(0,1,0), planet.getOrientation().getU()));
        gl.glRotated(phi, 0, 0, 1);

        if ("Earth".equals(planet.getName())) {
            logger.debug("axialTilt = {}, rotate = {}", axialTilt, phi);
        }

        gl.glCallList(this.listIndex);
        gl.glPopMatrix();

        super.draw(drawable);
    }
}
