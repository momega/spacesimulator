/**
 * 
 */
package com.momega.spacesimulator.renderer;

import java.awt.Point;

import javax.media.opengl.GL2;

import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.opengl.GLUtils;

/**
 * @author martin
 *
 */
public abstract class AbstractPositionProviderRenderer extends AbstractTextRenderer {

	protected void drawPositionProvider(GL2 gl, PositionProvider positionProvider, double color[]) {
        if (RendererModel.getInstance().isVisibleOnScreen(positionProvider)) {
            GLUtils.drawPoint(gl, 8, color, positionProvider);
        }
    }
	
	protected void renderPositionProvider(PositionProvider positionProvider) {
		ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(positionProvider);
        if (viewCoordinates!=null && viewCoordinates.isVisible()) {
        	Point size = getTextSize(positionProvider.getName());
            drawString(positionProvider.getName(), viewCoordinates.getPoint().getX() - size.getX() / 2.0, viewCoordinates.getPoint().getY() - 16);
        }
    }
}
