package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.MovingObject;
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

            // the order is important, at first move to focus
            GLUtils.translate(gl, getKeplerianElements().getCentralObject().getCartesianState().getPosition());
            GLUtils.rotate(gl, getKeplerianElements());

            double a = getKeplerianElements().getSemimajorAxis();
            double e = a * getKeplerianElements().getEccentricity();

            logger.debug("semi-major = {}", a);

            gl.glColor3dv(getTrajectory().getColor(), 0);

            gl.glLineWidth(1.5f);
            gl.glTranslated(-e, 0, 0); // move from foci to center
            if (getKeplerianElements().getEccentricity() < 1) {
            	double b = a * Math.sqrt(1 - getKeplerianElements().getEccentricity() * getKeplerianElements().getEccentricity());
                GLUtils.drawEllipse(gl, a, b, 7200);
            } else {
            	double b = a * Math.sqrt(getKeplerianElements().getEccentricity() * getKeplerianElements().getEccentricity() - 1);
                double HA = getKeplerianElements().getHyperbolicAnomaly();
                GLUtils.drawHyperbolaPartial(gl, a, b, -2 * Math.PI, -HA, 7200); // -HA because of a<0
            }

            gl.glPopMatrix();
        }

        // TODO: temporary here
        if ("Moon".equals(movingObject.getName())) {
            GL2 gl = drawable.getGL().getGL2();
            gl.glPushMatrix();

            gl.glColor3dv(getTrajectory().getColor(), 0);
            // the order is important
            GLUtils.translate(gl, getKeplerianElements().getCentralObject().getCartesianState().getPosition());
            GLUtils.rotate(gl, getKeplerianElements());

            GLUtils.drawBeansAndCircles(gl, 0, 0,  20 * 1E6, 18, 10);

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
