package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.ManeuverPoint;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.service.ManeuverService;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

/**
 * Created by martin on 10/11/14.
 */
public abstract class AbstractManeuverBitmapRenderer extends PositionProvidersBitmapRenderer {

    private final ManeuverService maneuverService;
    private final Spacecraft spacecraft;

    protected AbstractManeuverBitmapRenderer(Spacecraft spacecraft, ImageIcon imageIcon) {
        super(imageIcon);
        this.spacecraft = spacecraft;
        maneuverService = Application.getInstance().getService(ManeuverService.class);
    }

    protected Maneuver findManeuver() {
        return maneuverService.findActiveOrNext(spacecraft, ModelHolder.getModel().getTime());
    }

    @Override
    protected List<PositionProvider> getPositionProviders() {
        if (getManeuverPoint() != null) {
            return Collections.<PositionProvider>singletonList(getManeuverPoint());
        } else {
            return Collections.emptyList();
        }
    }

    protected abstract ManeuverPoint getManeuverPoint();

}
