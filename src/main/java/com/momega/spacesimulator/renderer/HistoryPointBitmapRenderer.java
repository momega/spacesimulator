package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.swing.Icons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 10/11/14.
 */
public class HistoryPointBitmapRenderer extends PositionProvidersBitmapRenderer {

    private final Spacecraft spacecraft;

    protected HistoryPointBitmapRenderer(Spacecraft spacecraft) {
        super(Icons.HISTORY_POINT);
        this.spacecraft = spacecraft;
    }

    @Override
    protected List<PositionProvider> getPositionProviders() {
        List<PositionProvider> list = new ArrayList<>();
        list.addAll(spacecraft.getHistoryTrajectory().getNamedHistoryPoints());
        return list;
    }
}
