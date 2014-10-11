package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.swing.Icons;

import javax.swing.*;

/**
 * Created by martin on 10/11/14.
 */
public class PeriapsisBitmapRenderer extends AbstractApsisBitmapRenderer {

    protected PeriapsisBitmapRenderer(Spacecraft spacecraft) {
        super(spacecraft, Icons.PERIAPSIS_POINT);
    }

    @Override
    protected Apsis getApsis() {
        return getSpacecraft().getTrajectory().getPeriapsis();
    }
}
