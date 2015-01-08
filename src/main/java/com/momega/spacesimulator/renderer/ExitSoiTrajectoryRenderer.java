package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.ExitSoiOrbitalPoint;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.Spacecraft;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Created by martin on 10/24/14.
 */
public class ExitSoiTrajectoryRenderer extends AbstractKeplerianTrajectoryRenderer {

    private final Spacecraft spacecraft;

    public ExitSoiTrajectoryRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
    	if (RendererModel.getInstance().isVisibleOnScreen(spacecraft)) {
            ExitSoiOrbitalPoint exitSoiOrbitalPoint = spacecraft.getExitSoiOrbitalPoint();
            if (exitSoiOrbitalPoint != null) {
	            KeplerianElements keplerianElements = exitSoiOrbitalPoint.getPredictedKeplerianElements();
	            if (keplerianElements != null) {
	                GL2 gl = drawable.getGL().getGL2();
	                gl.glPushMatrix();
	
	                drawTrajectory(gl, keplerianElements);
	
	                gl.glPopMatrix();
	            }
            }
        }
    }

    @Override
    public double[] getColor() {
        return new double[] { 1, 1, 0.4};
    }

}
