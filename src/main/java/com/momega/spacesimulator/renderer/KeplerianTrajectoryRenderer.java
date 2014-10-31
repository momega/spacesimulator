package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.MovingObject;
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

            if (movingObject instanceof Spacecraft) {
                gl.glEnable(GL2.GL_STENCIL_TEST);
                gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE);

                gl.glStencilFunc(GL2.GL_ALWAYS, 1, 0xff);
            }

            drawTrajectory(gl, movingObject.getKeplerianElements());

            if (movingObject instanceof Spacecraft) {
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
