package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.*;

/**
 * Created by martin on 6/8/14.
 */
public abstract class AbstractDynamicalPointRenderer extends AbstractTextRenderer {

    protected void drawData(NamedObject namedObject, Camera camera, int x, int y) {
        drawText(namedObject.getName(), x + 5, y + 60);
        drawText("P:" + namedObject.getPosition().toString(), x + 5, y + 50);
        drawText("N:" + namedObject.getOrientation().getN().toString(), x + 5, y + 40);
        drawText("U:" + namedObject.getOrientation().getU().toString(), x + 5, y + 30);
        drawText("V:" + namedObject.getOrientation().getV().toString(), x + 5, y + 20);
        double distance = namedObject.getPosition().subtract(camera.getPosition()).length() / 1E6;
        drawText("D:" + String.format("%6.2f Mm", distance), x + 5, y);
        if (namedObject instanceof MovingObject) {
            MovingObject movingObject = (MovingObject) namedObject;
            drawText("V:" + String.format("%6.2f m/s", movingObject.getVelocity().length()), x + 5, y + 10);
        }
        if (namedObject instanceof Satellite) {
            Satellite satellite = (Satellite) namedObject;
            drawText("VR:" + String.format("%6.2f m/s", satellite.getRelativeVelocity().length()), x + 5, y - 10);
            drawText("DR:" + String.format("%6.2f Mm", satellite.getRelativePosition().length() / 1E6), x + 5, y - 20);
        }
    }
}
