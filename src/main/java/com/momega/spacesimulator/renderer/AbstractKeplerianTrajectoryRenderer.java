package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.opengl.GLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;

/**
 * Created by martin on 10/24/14.
 */
public abstract class AbstractKeplerianTrajectoryRenderer extends AbstractRenderer {

    private static final Logger logger = LoggerFactory.getLogger(AbstractKeplerianTrajectoryRenderer.class);

    protected void drawTrajectory(GL2 gl, KeplerianElements keplerianElements) {
        // the order is important, at first move to focus
        GLUtils.translate(gl, keplerianElements.getKeplerianOrbit().getReferenceFrame().getCartesianState().getPosition());
        GLUtils.rotate(gl, keplerianElements);

        double a = keplerianElements.getKeplerianOrbit().getSemimajorAxis();
        double e = a * keplerianElements.getKeplerianOrbit().getEccentricity();

        logger.debug("semi-major = {}", a);

        gl.glLineWidth(1f);
        gl.glTranslated(-e, 0, 0); // move from foci to center
        if (keplerianElements.getKeplerianOrbit().getEccentricity() < 1) {
            double b = keplerianElements.getKeplerianOrbit().getSemiminorAxis();
            GLUtils.drawEllipse(gl, a, b, 7200, getColor());
        } else {
            double b = keplerianElements.getKeplerianOrbit().getSemiminorAxis();
            double HA = keplerianElements.getHyperbolicAnomaly();
            GLUtils.drawHyperbolaPartial(gl, a, b, -2 * Math.PI, -HA, 7200, getColor()); // -HA because of a<0
        }

    }

    public abstract double[] getColor();
}
