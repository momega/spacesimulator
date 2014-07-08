package com.momega.spacesimulator.renderer;

import com.jogamp.opengl.util.texture.Texture;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.utils.VectorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import static javax.media.opengl.GL.GL_LINES;

/**
 * The class renders the celestial body. It holds the {@link CelestialBody} instance and contains logic for rendering.
 * Created by martin on 4/19/14.
 */
public class CelestialBodyRenderer extends AbstractRenderer {

    private static final Logger logger = LoggerFactory.getLogger(CelestialBodyRenderer.class);

    private final CelestialBody celestialBody;
    private Texture texture;
    private int listIndex;

    public CelestialBodyRenderer(CelestialBody celestialBody) {
        this.celestialBody = celestialBody;
    }

    public void loadTexture(GL2 gl) {
        this.texture = GLUtils.loadTexture(gl, getClass(), celestialBody.getTextureFileName());
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
        glu.gluSphere(quadric, celestialBody.getRadius(), 320, 320);
        glu.gluDeleteQuadric(quadric);
        texture.disable(gl);

        gl.glLineWidth(2f);
        gl.glBegin(GL_LINES);
        gl.glVertex3d(0, 0, celestialBody.getRadius() * 1.2);
        gl.glVertex3d(0, 0, -celestialBody.getRadius() * 1.2);
        gl.glEnd();
    }

    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();

        GLUtils.translate(gl, celestialBody.getPosition());

        double axialTilt = Math.toDegrees(VectorUtils.angleBetween(new Vector3d(0, 0, 1), celestialBody.getOrientation().getV()));
        gl.glRotated(axialTilt, 1, 0, 0);
        //TODO: fix the phi angle
        double phi = Math.toDegrees(VectorUtils.angleBetween(new Vector3d(0, 1, 0), celestialBody.getOrientation().getU()));
        gl.glRotated(phi, 0, 0, 1);

        logger.debug("axialTilt = {}, rotate = {}", axialTilt, phi);

        gl.glCallList(this.listIndex);
        gl.glPopMatrix();
    }
}
