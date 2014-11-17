package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.PhysicalBody;
import com.momega.spacesimulator.model.Spacecraft;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * The renderer of keplerian trajectory 2d
 * Created by martin on 4/21/14.
 */
public class KeplerianTrajectoryRenderer extends AbstractKeplerianTrajectoryRenderer {

    private final MovingObject movingObject;

    public KeplerianTrajectoryRenderer(MovingObject movingObject) {
        this.movingObject = movingObject;
    }

    public void draw(GLAutoDrawable drawable) {
        if (RendererModel.getInstance().isVisibleOnScreen(movingObject)) {
            GL2 gl = drawable.getGL().getGL2();
            gl.glPushMatrix();

            if (movingObject instanceof PhysicalBody) {
            	PhysicalBody spacecraft  = (PhysicalBody) movingObject;
                gl.glEnable(GL2.GL_STENCIL_TEST);
                gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE);

                gl.glStencilFunc(GL2.GL_ALWAYS, spacecraft.getIndex(), 0xff); // 1 is variable
            }

            drawTrajectory(gl, movingObject.getKeplerianElements());

            if (movingObject instanceof PhysicalBody) {
                gl.glDisable(GL2.GL_STENCIL_TEST);
            }

            gl.glPopMatrix();
        }
    }

    @Override
    public double[] getColor() {
        return movingObject.getTrajectory().getColor();
    }
}
