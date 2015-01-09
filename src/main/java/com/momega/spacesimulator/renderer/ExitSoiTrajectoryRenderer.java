package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.ExitSoiOrbitalPoint;
import com.momega.spacesimulator.model.FutureMovingObject;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.ReferenceFrame;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;

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
	                if (exitSoiOrbitalPoint.getClosestPoint()!=null) {
	                	drawBezier(gl, exitSoiOrbitalPoint);
	                	
	                	ReferenceFrame referenceFrame = exitSoiOrbitalPoint.getClosestPoint().getKeplerianElements().getKeplerianOrbit().getReferenceFrame();
	                	if (referenceFrame instanceof FutureMovingObject) {
	                		gl.glPushMatrix();
	                		GLUtils.translate(gl, referenceFrame.getPosition());
	                		CelestialBody body = (CelestialBody) ((FutureMovingObject) referenceFrame).getMovingObject();
	                		gl.glColor3dv(body.getTrajectory().getColor(), 0);
	                		GLUtils.drawSphere(gl, body.getRadius(), false);
	                		gl.glPopMatrix();
	                	}
	                }
	            }
            }
        }
    }
    
    protected void drawBezier(GL2 gl, ExitSoiOrbitalPoint exitSoiOrbitalPoint) {
    	gl.glPushMatrix();
    	Apsis closestPoint = exitSoiOrbitalPoint.getClosestPoint();
    	Vector3d start = exitSoiOrbitalPoint.getKeplerianElements().getCartesianPosition();
    	Vector3d end = closestPoint.getKeplerianElements().getCartesianPosition();
    	
    	CartesianState relative = exitSoiOrbitalPoint.getKeplerianElements().toCartesianState().subtract(spacecraft.getKeplerianElements().getKeplerianOrbit().getReferenceFrame().getCartesianState());
    	Vector3d startCtrlPoint = relative.getPosition().scaleAdd(10000, relative.getVelocity());
    	startCtrlPoint = startCtrlPoint.add(spacecraft.getKeplerianElements().getKeplerianOrbit().getReferenceFrame().getCartesianState().getPosition());
    	
    	double timeDist = closestPoint.getTimestamp().subtract(exitSoiOrbitalPoint.getTimestamp());
    	
    	Timestamp t = closestPoint.getTimestamp().add(-timeDist/20);
    	KeplerianElements endCtrlKeplerianElements = closestPoint.getKeplerianElements().shiftTo(t);
    	Vector3d endCtrlPoint = endCtrlKeplerianElements.getCartesianPosition();
    	
        GLUtils.drawBezier(gl, start, startCtrlPoint, endCtrlPoint, end, getColor());
        gl.glPopMatrix();
    }

    @Override
    public double[] getColor() {
        return new double[] { 1, 1, 0.4};
    }

}
