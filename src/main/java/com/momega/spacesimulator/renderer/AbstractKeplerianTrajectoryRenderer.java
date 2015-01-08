package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;

import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.opengl.GLUtils;

/**
 * Created by martin on 10/24/14.
 */
public abstract class AbstractKeplerianTrajectoryRenderer extends AbstractRenderer {

    protected void drawTrajectory(GL2 gl, KeplerianElements keplerianElements) {
        setMatrix(gl, keplerianElements);
        drawOnlyTrajectory(gl, keplerianElements);
    }
    
    protected void setMatrix(GL2 gl, KeplerianElements keplerianElements) {
    	GLUtils.translate(gl, keplerianElements.getKeplerianOrbit().getReferenceFrame().getCartesianState().getPosition());
        GLUtils.rotate(gl, keplerianElements);
    }
    
    protected void drawOnlyTrajectory(GL2 gl, KeplerianElements keplerianElements) {
        double a = keplerianElements.getKeplerianOrbit().getSemimajorAxis();
        double e = a * keplerianElements.getKeplerianOrbit().getEccentricity();

        gl.glLineWidth(1f);
        gl.glTranslated(-e, 0, 0); // move from foci to center
        if (!keplerianElements.getKeplerianOrbit().isHyperbolic()) {
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
