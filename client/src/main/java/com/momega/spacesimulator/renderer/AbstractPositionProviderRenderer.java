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

	protected void renderPositionProvider(GL2 gl, PositionProvider positionProvider) {
		ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(positionProvider);
        if (viewCoordinates!=null && viewCoordinates.isVisible()) {
        	Point size = getTextSize(positionProvider.getName());
            if (GLUtils.checkDepth(gl, viewCoordinates)) {
                drawString(positionProvider.getName(), viewCoordinates.getPoint().getX() - size.getX() / 2.0, viewCoordinates.getPoint().getY() - 16);
            }
        }
    }
}
