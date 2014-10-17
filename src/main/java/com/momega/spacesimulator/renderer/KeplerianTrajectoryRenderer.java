package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Trajectory;
import com.momega.spacesimulator.opengl.GLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * The renderer of keplerian trajectory 2d
 * Created by martin on 4/21/14.
 */
public class KeplerianTrajectoryRenderer extends AbstractRenderer {

    private static final Logger logger = LoggerFactory.getLogger(KeplerianTrajectoryRenderer.class);
    private final MovingObject movingObject;

    public KeplerianTrajectoryRenderer(MovingObject movingObject) {
        this.movingObject = movingObject;
    }

    public void draw(GLAutoDrawable drawable) {
        if (RendererModel.getInstance().isVisibleOnScreen(movingObject)) {
            GL2 gl = drawable.getGL().getGL2();
            gl.glPushMatrix();

            if (movingObject instanceof Spacecraft) {
                gl.glEnable(GL2.GL_STENCIL_TEST);
                gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE);

                gl.glStencilFunc(GL2.GL_ALWAYS, 1, 0xff);
            }

            // the order is important, at first move to focus
            GLUtils.translate(gl, getKeplerianElements().getKeplerianOrbit().getCentralObject().getCartesianState().getPosition());
            GLUtils.rotate(gl, getKeplerianElements());

            double a = getKeplerianElements().getKeplerianOrbit().getSemimajorAxis();
            double e = a * getKeplerianElements().getKeplerianOrbit().getEccentricity();

            logger.debug("semi-major = {}", a);

            gl.glLineWidth(1.5f);
            gl.glTranslated(-e, 0, 0); // move from foci to center
            if (getKeplerianElements().getKeplerianOrbit().getEccentricity() < 1) {
            	double b = a * Math.sqrt(1 - getKeplerianElements().getKeplerianOrbit().getEccentricity() * getKeplerianElements().getKeplerianOrbit().getEccentricity());
                GLUtils.drawEllipse(gl, a, b, 7200, getTrajectory().getColor());
            } else {
            	double b = a * Math.sqrt(getKeplerianElements().getKeplerianOrbit().getEccentricity() * getKeplerianElements().getKeplerianOrbit().getEccentricity() - 1);
                double HA = getKeplerianElements().getHyperbolicAnomaly();
                GLUtils.drawHyperbolaPartial(gl, a, b, -2 * Math.PI, -HA, 7200, getTrajectory().getColor()); // -HA because of a<0
            }

            if (movingObject instanceof Spacecraft) {
                gl.glDisable(GL2.GL_STENCIL_TEST);
            }

            gl.glPopMatrix();
        }
    }

    public KeplerianElements getKeplerianElements() {
        return movingObject.getKeplerianElements();
    }

    public Trajectory getTrajectory() {
        return movingObject.getTrajectory();
    }
}
