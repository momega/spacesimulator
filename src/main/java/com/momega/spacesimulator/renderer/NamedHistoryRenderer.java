package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.Spacecraft;

import javax.media.opengl.GL2;

/**
 * Created by martin on 8/24/14.
 */
public class NamedHistoryRenderer extends AbstractPositionProviderRenderer {

    private final Spacecraft spacecraft;

    public NamedHistoryRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        for(HistoryPoint hp : spacecraft.getNamedHistoryPoints()) {
            renderPositionProvider(gl, hp);
        }
    }
}
