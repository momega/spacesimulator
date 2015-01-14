package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Vector3d;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 10/11/14.
 */
public abstract class PositionProvidersBitmapRenderer extends AbstractBitmapRenderer {

    private final ImageIcon imageIcon;

    protected PositionProvidersBitmapRenderer(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    protected abstract List<PositionProvider> getPositionProviders();

    @Override
    public List<Vector3d> getPositions() {
        List<Vector3d> list = new ArrayList<>();
        for(PositionProvider positionProvider : getPositionProviders()) {
            list.add(positionProvider.getPosition());
        }
        return list;
    }

    @Override
    protected ImageIcon getImageIcon() {
        return imageIcon;
    }
}
