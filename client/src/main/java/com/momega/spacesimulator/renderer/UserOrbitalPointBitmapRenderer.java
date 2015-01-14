package com.momega.spacesimulator.renderer;

import java.util.ArrayList;
import java.util.List;

import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.swing.Icons;

/**
 * Created by martin on 10/16/14.
 */
public class UserOrbitalPointBitmapRenderer extends PositionProvidersBitmapRenderer {

    private final MovingObject movingObject;

    protected UserOrbitalPointBitmapRenderer(MovingObject movingObject) {
        super(Icons.USER_POINT);
        this.movingObject = movingObject;
    }

    @Override
    protected List<PositionProvider> getPositionProviders() {
        List<PositionProvider> list = new ArrayList<>();
        list.addAll(movingObject.getUserOrbitalPoints());
        return list;
    }

}
