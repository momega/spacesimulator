package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.swing.Icons;

/**
 * Created by martin on 10/11/14.
 */
public class PeriapsisBitmapRenderer extends AbstractApsisBitmapRenderer {

    protected PeriapsisBitmapRenderer(MovingObject spacecraft) {
        super(spacecraft, Icons.PERIAPSIS_POINT);
    }

    @Override
    protected Apsis getApsis() {
        return getSpacecraft().getTrajectory().getPeriapsis();
    }
}
