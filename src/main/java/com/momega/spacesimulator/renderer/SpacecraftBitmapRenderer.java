package com.momega.spacesimulator.renderer;

import java.util.Collections;
import java.util.List;

import com.momega.spacesimulator.model.PhysicalBody;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.swing.SwingUtils;

/**
 * Created by martin on 10/11/14.
 */
public class SpacecraftBitmapRenderer extends PositionProvidersBitmapRenderer {

    private final PhysicalBody spacecraft;

    public SpacecraftBitmapRenderer(Spacecraft spacecraft) {
        super(SwingUtils.createImageIcon(spacecraft.getIcon()));
        this.spacecraft = spacecraft;
    }

    @Override
    protected List<PositionProvider> getPositionProviders() {
        return Collections.singletonList((PositionProvider) spacecraft);
    }

}
