package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.opengl.GLUtils;

/**
 * Created by martin on 8/24/14.
 */
public class NamedHistoryRenderer extends AbstractPositionProviderRenderer {

    private final Spacecraft spacecraft;

    public NamedHistoryRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    @Override
    protected void drawObjects(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        GLUtils.drawPoints(gl, 8, this.spacecraft.getHistoryTrajectory().getColor(),
        		spacecraft.getHistoryTrajectory().getNamedHistoryPoints());
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        for(HistoryPoint hp : spacecraft.getHistoryTrajectory().getNamedHistoryPoints()) {
            renderPositionProvider(hp);
        }
    }
}
