/**
 * 
 */
package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.AbstractOrbitalPoint;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.springframework.util.Assert;

import javax.media.opengl.GL2;
import java.awt.*;

/**
 * @author martin
 *
 */
public abstract class AbstractOrbitalPositionProviderRenderer extends AbstractPositionProviderRenderer {

	@Override
	protected void renderPositionProvider(GL2 gl, PositionProvider positionProvider) {
		ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(positionProvider);
        if (viewCoordinates!=null && viewCoordinates.isVisible()) {
    		Assert.isInstanceOf(AbstractOrbitalPoint.class, positionProvider);
            AbstractOrbitalPoint orbitalPositionProvider = (AbstractOrbitalPoint) positionProvider;
            if (GLUtils.checkDepth(gl, viewCoordinates)) {
                Point size = getTextSize(orbitalPositionProvider.getName());
                drawString(orbitalPositionProvider.getName(), viewCoordinates.getPoint().getX() - size.getX() / 2.0, viewCoordinates.getPoint().getY() - 16);
                String etaTime = TimeUtils.periodAsString(orbitalPositionProvider);
                size = getTextSize(etaTime);
                drawString(etaTime, viewCoordinates.getPoint().getX() - size.getX() / 2.0, viewCoordinates.getPoint().getY() - 26);
            }
        }
    }
}
