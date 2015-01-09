package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.momega.spacesimulator.model.ExitSoiOrbitalPoint;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.opengl.GLUtils;

/**
 * The renderer of keplerian trajectory 2d
 * Created by martin on 4/21/14.
 */
public class KeplerianTrajectoryRenderer extends AbstractKeplerianTrajectoryRenderer {

    private final Spacecraft spacecraft;

    public KeplerianTrajectoryRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    public void draw(GLAutoDrawable drawable) {
        if (RendererModel.getInstance().isVisibleOnScreen(spacecraft)) {
            GL2 gl = drawable.getGL().getGL2();
            gl.glPushMatrix();

            gl.glEnable(GL2.GL_STENCIL_TEST);
            gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE);

            gl.glStencilFunc(GL2.GL_ALWAYS, spacecraft.getIndex(), 0xff); // 1 is variable

            drawTrajectory(gl, spacecraft.getKeplerianElements());
            gl.glDisable(GL2.GL_STENCIL_TEST);

            gl.glPopMatrix();
        }
    }
    
    protected void drawOnlyTrajectory(GL2 gl, KeplerianElements keplerianElements) {
        double a = keplerianElements.getKeplerianOrbit().getSemimajorAxis();
        double e = a * keplerianElements.getKeplerianOrbit().getEccentricity();
        double b = keplerianElements.getKeplerianOrbit().getSemiminorAxis();
        
        gl.glLineWidth(1f);
        gl.glTranslated(-e, 0, 0); // move from foci to center
        if (!keplerianElements.getKeplerianOrbit().isHyperbolic()) {
        	ExitSoiOrbitalPoint exitSoiOrbitalPoint = spacecraft.getExitSoiOrbitalPoint();
        	if (exitSoiOrbitalPoint==null) {
	            GLUtils.drawEllipse(gl, a, b, 7200, getColor());
        	} else {
        		double startAngle = spacecraft.getKeplerianElements().getEccentricAnomaly();
        		double endAngle = exitSoiOrbitalPoint.getKeplerianElements().getEccentricAnomaly();
        		if (startAngle > endAngle) {
        			startAngle -= 2*Math.PI;
        		}
        		GLUtils.drawEllipse(gl, a, b, startAngle, endAngle, 7200, getColor());
        	}
        } else {
            double HA = keplerianElements.getHyperbolicAnomaly();
            GLUtils.drawHyperbolaPartial(gl, a, b, -2 * Math.PI, -HA, 7200, getColor()); // -HA because of a<0
        }
    }

    @Override
    public double[] getColor() {
        return spacecraft.getTrajectory().getColor();
    }
}
