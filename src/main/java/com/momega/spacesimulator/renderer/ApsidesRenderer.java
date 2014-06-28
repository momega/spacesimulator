package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Satellite;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.utils.MathUtils;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Created by martin on 6/15/14.
 */
public class ApsidesRenderer extends AbstractTextRenderer {

    private final Satellite satellite;

    public ApsidesRenderer(Satellite satellite) {
        this.satellite = satellite;
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPointSize(8);
        gl.glColor3dv(this.satellite.getTrajectory().getColor(), 0);
        gl.glBegin(GL2.GL_POINTS);

        KeplerianElements keplerianElements = satellite.getKeplerianElements();

        double rp = keplerianElements.getSemimajorAxis()* (1 - keplerianElements.getEccentricity());
        double ra = keplerianElements.getSemimajorAxis()* (1 + keplerianElements.getEccentricity());

        Vector3d periapsis = MathUtils.getKeplerianPosition(keplerianElements, rp, 0d);
        gl.glVertex3dv(periapsis.asArray(), 0);

        if (keplerianElements.getEccentricity()<1) {
            Vector3d apoapsis = MathUtils.getKeplerianPosition(keplerianElements, ra, Math.PI);
            gl.glVertex3dv(apoapsis.asArray(), 0);
        }

        gl.glEnd();

        super.draw(drawable);
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
//        if (periapsisCoordinates != null) {
//            drawText("P", (int)periapsisCoordinates[0], (int)periapsisCoordinates[1]);
//        }
//        if (apoapsisCoordinates != null) {
//            drawText("A", (int)apoapsisCoordinates[0], (int)apoapsisCoordinates[1]);
//        }
    }
}
