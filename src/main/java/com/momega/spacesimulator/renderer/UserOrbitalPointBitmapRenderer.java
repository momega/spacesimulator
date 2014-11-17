package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.PhysicalBody;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.swing.Icons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 10/16/14.
 */
public class UserOrbitalPointBitmapRenderer extends PositionProvidersBitmapRenderer {

    private final PhysicalBody physicalBody;

    protected UserOrbitalPointBitmapRenderer(PhysicalBody physicalBody) {
        super(Icons.USER_POINT);
        this.physicalBody = physicalBody;
    }

    @Override
    protected List<PositionProvider> getPositionProviders() {
        List<PositionProvider> list = new ArrayList<>();
        list.addAll(physicalBody.getUserOrbitalPoints());
        return list;
    }

}
