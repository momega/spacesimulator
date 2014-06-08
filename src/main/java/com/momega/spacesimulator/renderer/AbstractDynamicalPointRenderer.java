package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.model.Vector3d;

/**
 * Created by martin on 6/8/14.
 */
public abstract class AbstractDynamicalPointRenderer extends AbstractTextRenderer {

    protected void drawData(DynamicalPoint dynamicalPoint, Camera camera, int x, int y) {
        drawText(dynamicalPoint.getName(), x + 5, y + 5);
        drawText("P:" + dynamicalPoint.getPosition().toString(), x + 5, y - 8);
        drawText("V:" + String.format("%6.2f", dynamicalPoint.getVelocity().length()), x + 5, y - 18);
        double distance = dynamicalPoint.getPosition().subtract(camera.getPosition()).length() / 1E9;
        drawText("D:" + String.format("%6.2f", distance), x + 5, y - 28);
    }
}
