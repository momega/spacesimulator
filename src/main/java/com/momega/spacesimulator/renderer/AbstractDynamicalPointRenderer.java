package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.model.Satellite;
import com.momega.spacesimulator.model.Vector3d;

/**
 * Created by martin on 6/8/14.
 */
public abstract class AbstractDynamicalPointRenderer extends AbstractTextRenderer {

    protected void drawData(DynamicalPoint dynamicalPoint, Camera camera, int x, int y) {
        drawText(dynamicalPoint.getName(), x + 5, y + 30);
        drawText("P:" + dynamicalPoint.getPosition().toString(), x + 5, y + 20);
        drawText("V:" + String.format("%6.2f m/s", dynamicalPoint.getVelocity().length()), x + 5, y + 10);
        double distance = dynamicalPoint.getPosition().subtract(camera.getPosition()).length() / 1E6;
        drawText("D:" + String.format("%6.2f Mm", distance), x + 5, y);
        if (dynamicalPoint instanceof Satellite) {
            Satellite satellite = (Satellite) dynamicalPoint;
            drawText("VR:" + String.format("%6.2f m/s", satellite.getRelativeVelocity().length()), x + 5, y - 10);
            drawText("DR:" + String.format("%6.2f Mm", satellite.getRelativePosition().length() / 1E6), x + 5, y - 20);
        }
    }
}
