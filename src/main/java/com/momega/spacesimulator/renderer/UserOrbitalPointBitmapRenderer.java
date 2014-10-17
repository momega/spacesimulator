package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.swing.Icons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 10/16/14.
 */
public class UserOrbitalPointBitmapRenderer extends PositionProvidersBitmapRenderer {

    private final Spacecraft spacecraft;

    protected UserOrbitalPointBitmapRenderer(Spacecraft spacecraft) {
        super(Icons.USER_POINT);
        this.spacecraft = spacecraft;
    }

    @Override
    protected List<PositionProvider> getPositionProviders() {
        List<PositionProvider> list = new ArrayList<>();
        list.addAll(spacecraft.getUserOrbitalPoints());
        return list;
    }

}
