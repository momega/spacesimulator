package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.swing.Icons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 10/11/14.
 */
public class ClosestPointBitmapRenderer extends PositionProvidersBitmapRenderer {

    private final Spacecraft spacecraft;

    protected ClosestPointBitmapRenderer(Spacecraft spacecraft) {
        super(Icons.CLOSEST_POINT);
        this.spacecraft = spacecraft;
    }

    @Override
    protected List<PositionProvider> getPositionProviders() {
        List<PositionProvider> list = new ArrayList<>();
        if (spacecraft.getTarget() != null && spacecraft.getTarget().getClosestPoint()!=null) {
            list.add(spacecraft.getTarget().getClosestPoint());
        }
        return list;
    }
}
