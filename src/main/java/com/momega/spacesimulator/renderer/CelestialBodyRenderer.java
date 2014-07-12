package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.ModelHolder;
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

/**
 * The class renders the celestial body. It holds the {@link CelestialBody} instance and contains logic for rendering.
 * Created by martin on 4/19/14.
 */
public class CelestialBodyRenderer extends AbstractTextureRenderer {

    private static final Logger logger = LoggerFactory.getLogger(CelestialBodyRenderer.class);

    private final CelestialBody celestialBody;

    public CelestialBodyRenderer(CelestialBody celestialBody) {
        this.celestialBody = celestialBody;
    }

    @Override
    protected void loadTexture(GL2 gl) {
        loadTexture(gl, celestialBody.getTextureFileName());
    }

    @Override
    protected void drawObject(GL2 gl) {

        gl.glDisable(GL2.GL_BLEND);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glShadeModel(GL2.GL_SMOOTH);

        GLU glu = new GLU();
        GLUquadric quadric = glu.gluNewQuadric();
        gl.glColor3d(1, 1, 1);
        glu.gluQuadricTexture(quadric, true);
        glu.gluQuadricNormals(quadric, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(quadric, GLU.GLU_OUTSIDE);
        glu.gluSphere(quadric, celestialBody.getRadius(), 64, 64);
        glu.gluDeleteQuadric(quadric);

        gl.glShadeModel(GL2.GL_FLAT);
        gl.glDisable(GL2.GL_CULL_FACE);
    }

    @Override
    protected void prepareObject(GL2 gl) {
        super.prepareObject(gl);

        gl.glLineWidth(2f);
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3d(0, 0, celestialBody.getRadius() * 1.2);
        gl.glVertex3d(0, 0, -celestialBody.getRadius() * 1.2);
        gl.glEnd();
    }

    public void setMatrix(GL2 gl ) {
        GLUtils.translate(gl, celestialBody.getPosition());

        double axialTilt = Math.toDegrees(VectorUtils.angleBetween(new Vector3d(0, 0, 1), celestialBody.getOrientation().getV()));
        gl.glRotated(axialTilt, 1, 0, 0);

        logger.debug("N = {}", celestialBody.getOrientation().getN().asArray());
        double phi = Math.toDegrees(VectorUtils.angleBetween(new Vector3d(1, 0, 0), celestialBody.getOrientation().getN()));
        if (celestialBody.getOrientation().getN().z<0) {
            phi = 360 - phi;
        }
        logger.debug("axialTilt = {}, rotate = {}", axialTilt, phi);
        gl.glRotated(phi, 0, 0, 1);
    }

    @Override
    protected void additionalDraw(GL2 gl) {
        super.additionalDraw(gl);

        if (ModelHolder.getModel().getSelectedDynamicalPoint() == celestialBody) {
            for (int i = 1; i <= 5; i++) {
                GLUtils.drawCircle(gl, 0, 0, celestialBody.getRadius() * i, 360);
            }
            GLUtils.drawBeams(gl, 0, 0, celestialBody.getRadius() * 5, 18);
        }
    }
}
