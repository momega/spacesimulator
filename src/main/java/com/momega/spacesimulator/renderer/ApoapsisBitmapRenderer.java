package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.PhysicalBody;
import com.momega.spacesimulator.swing.Icons;

/**
 * Created by martin on 10/11/14.
 */
public class ApoapsisBitmapRenderer extends AbstractApsisBitmapRenderer {

    protected ApoapsisBitmapRenderer(PhysicalBody spacecraft) {
        super(spacecraft, Icons.APSIS_POINT);
    }

    @Override
    protected Apsis getApsis() {
        return getSpacecraft().getTrajectory().getApoapsis();
    }
}
