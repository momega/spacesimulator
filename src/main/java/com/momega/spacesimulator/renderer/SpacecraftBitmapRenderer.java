package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.swing.Icons;

import java.util.Collections;
import java.util.List;

/**
 * Created by martin on 10/11/14.
 */
public class SpacecraftBitmapRenderer extends PositionProvidersBitmapRenderer {

    private final Spacecraft spacecraft;

    public SpacecraftBitmapRenderer(Spacecraft spacecraft) {
        super(Icons.SPACECRAFT1);
        this.spacecraft = spacecraft;
    }

    @Override
    protected List<PositionProvider> getPositionProviders() {
        return Collections.singletonList((PositionProvider) spacecraft);
    }

}