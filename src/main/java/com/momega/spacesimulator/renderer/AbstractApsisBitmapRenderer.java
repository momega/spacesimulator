package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.PhysicalBody;
import com.momega.spacesimulator.model.PositionProvider;

import javax.swing.*;

import java.util.Collections;
import java.util.List;

/**
 * Created by martin on 10/11/14.
 */
public abstract class AbstractApsisBitmapRenderer extends PositionProvidersBitmapRenderer {

    private final PhysicalBody spacecraft;

    protected AbstractApsisBitmapRenderer(PhysicalBody spacecraft, ImageIcon imageIcon) {
        super(imageIcon);
        this.spacecraft = spacecraft;
    }

    protected abstract Apsis getApsis();

    public PhysicalBody getSpacecraft() {
        return spacecraft;
    }

    @Override
    protected List<PositionProvider> getPositionProviders() {
        if (getApsis() != null) {
            return Collections.<PositionProvider>singletonList(getApsis());
        } else {
            return Collections.emptyList();
        }
    }
}
