package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Spacecraft;

import javax.media.opengl.GL2;

/**
 * Created by martin on 10/11/14.
 */
public class SpacecraftBitmapRenderer extends PositionProviderBitmapRenderer {

    private static final String PICTURE = "/images/Number-1-icon.png";

    public SpacecraftBitmapRenderer(Spacecraft spacecraft) {
        super(spacecraft, PICTURE);
    }

}
