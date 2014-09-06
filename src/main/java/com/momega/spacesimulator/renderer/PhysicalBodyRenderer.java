package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.PhysicalBody;

import javax.media.opengl.GL2;
import java.awt.*;

/**
 * Renderer displays the point at the physica body position.
 * Created by martin on 4/28/14.
 */
public class PhysicalBodyRenderer extends AbstractTextRenderer {

    private final PhysicalBody physicalBody;

    public PhysicalBodyRenderer(PhysicalBody physicalBody) {
        this.physicalBody = physicalBody;
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(physicalBody);
        if (viewCoordinates.getRadius()<=5 && viewCoordinates.isVisible()) {
            Point size = getTextSize(physicalBody.getName());
            drawText(physicalBody.getName(), viewCoordinates.getPoint().getX() - size.getX() / 2.0, viewCoordinates.getPoint().getY() - 16);
        }
    }

}
