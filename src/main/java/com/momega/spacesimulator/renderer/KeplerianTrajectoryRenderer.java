package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Trajectory;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.utils.MathUtils;
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

            GLUtils.translate(gl, getKeplerianElements().getCentralObject().getCartesianState().getPosition());
            gl.glRotated(Math.toDegrees(getKeplerianElements().getAscendingNode()), 0, 0, 1);
            gl.glRotated(Math.toDegrees(getKeplerianElements().getInclination()), 1, 0, 0);
            gl.glRotated(Math.toDegrees(getKeplerianElements().getArgumentOfPeriapsis()), 0, 0, 1);

            double a = getKeplerianElements().getSemimajorAxis();
            double e = a * getKeplerianElements().getEccentricity();
            double b;
            if (getKeplerianElements().getEccentricity() < 1) {
                b = a * Math.sqrt(1 - getKeplerianElements().getEccentricity() * getKeplerianElements().getEccentricity());
            } else {
                b = a * Math.sqrt(getKeplerianElements().getEccentricity() * getKeplerianElements().getEccentricity() - 1);
            }

            logger.debug("semi-major = {}", a);

            gl.glColor3dv(getTrajectory().getColor(), 0);
            gl.glLineWidth(1.5f);
            gl.glTranslated(-e, 0, 0);
            if (getKeplerianElements().getEccentricity() < 1) {
                GLUtils.drawEllipse(gl, a, b, 7200);
            } else {
                double HA = getKeplerianElements().getHyperbolicAnomaly();
                GLUtils.drawHyperbolaPartial(gl, a, b, -2 * Math.PI, -HA, 7200); // -HA because of a<0
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
