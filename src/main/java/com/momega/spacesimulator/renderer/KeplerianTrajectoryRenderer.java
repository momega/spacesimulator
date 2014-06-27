package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianTrajectory3d;
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
public class KeplerianTrajectoryRenderer extends TrajectoryRenderer {

    private static final Logger logger = LoggerFactory.getLogger(KeplerianTrajectoryRenderer.class);

    public KeplerianTrajectoryRenderer(KeplerianTrajectory3d trajectory) {
        super(trajectory);
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        super.draw(drawable);
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();

        GLUtils.translate(gl, getTrajectory().getCentralObject().getPosition());
        gl.glRotated(Math.toDegrees(getTrajectory().getAscendingNode()), 0, 0, 1);
        gl.glRotated(Math.toDegrees(getTrajectory().getInclination()), 1, 0, 0);
        gl.glRotated(Math.toDegrees(getTrajectory().getArgumentOfPeriapsis()), 0, 0, 1);

        double a = getTrajectory().getSemimajorAxis();
        double e = a * getTrajectory().getEccentricity();
        double b;
        if (getTrajectory().getEccentricity()<1) {
            b = a * Math.sqrt(1 - getTrajectory().getEccentricity() * getTrajectory().getEccentricity());
        } else {
            b = a * Math.sqrt(getTrajectory().getEccentricity() * getTrajectory().getEccentricity() - 1);
        }

        logger.debug("semi-major = {}", a);

        gl.glColor3dv(getColor(), 0);
        gl.glLineWidth(1);
        gl.glTranslated(-e, 0, 0);
        if (getTrajectory().getEccentricity()<1) {
            GLUtils.drawEllipse(gl, a, b, 7200);
        } else {
            double HA = getHA();
            logger.debug("HA = {}", HA);
            GLUtils.drawHyperbolaPartial(gl, a, b, -Math.PI, -HA, 7200); // -HA because of a<0
        }

        gl.glPopMatrix();
    }

    protected double getHA() {
        double theta = getTrajectory().getTrueAnomaly();
        double eccentricity = getTrajectory().getEccentricity();
        double sinH = (Math.sin(theta) * Math.sqrt(eccentricity*eccentricity -1)) / (1 + eccentricity * Math.cos(theta));
        double HA = MathUtils.asinh(sinH);
        return HA;
    }

    @Override
    public KeplerianTrajectory3d getTrajectory() {
        return (KeplerianTrajectory3d) super.getTrajectory();
    }
}
