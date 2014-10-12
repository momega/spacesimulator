package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.ManeuverPoint;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.swing.Icons;

/**
 * Created by martin on 10/11/14.
 */
public class EndManeuverBitmapRenderer extends AbstractManeuverBitmapRenderer {

    protected EndManeuverBitmapRenderer(Spacecraft spacecraft) {
        super(spacecraft, Icons.END_MANEUVER_POINT);
    }

    @Override
    protected ManeuverPoint getManeuverPoint() {
        Maneuver m = findManeuver();
        if (m == null) {
            return null;
        }
        return m.getEnd();
    }
}
