package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Target;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Created by martin on 10/24/14.
 */
public class TargetTrajectoryRenderer extends AbstractKeplerianTrajectoryRenderer {

    private final Spacecraft spacecraft;

    public TargetTrajectoryRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        if (RendererModel.getInstance().isVisibleOnScreen(spacecraft)) {
            Target target = spacecraft.getTarget();
            KeplerianElements keplerianElements = target.getKeplerianElements();
            if (keplerianElements != null) {
                GL2 gl = drawable.getGL().getGL2();
                gl.glPushMatrix();

                drawTrajectory(gl, keplerianElements);

                gl.glPopMatrix();
            }
        }
    }

    @Override
    public double[] getColor() {
        return new double[] { 0, 1, 0};
    }

}
