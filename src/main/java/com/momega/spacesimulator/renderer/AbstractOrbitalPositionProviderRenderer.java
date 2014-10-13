/**
 * 
 */
package com.momega.spacesimulator.renderer;

import java.awt.Point;

import org.springframework.util.Assert;

import com.momega.spacesimulator.model.OrbitalPositionProvider;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * @author martin
 *
 */
public abstract class AbstractOrbitalPositionProviderRenderer extends AbstractPositionProviderRenderer {

	@Override
	protected void renderPositionProvider(PositionProvider positionProvider) {
		ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(positionProvider);
        if (viewCoordinates!=null && viewCoordinates.isVisible()) {
    		Assert.isInstanceOf(OrbitalPositionProvider.class, positionProvider);
    		OrbitalPositionProvider orbitalPositionProvider = (OrbitalPositionProvider) positionProvider;

    		Point size = getTextSize(orbitalPositionProvider.getName());
            drawString(orbitalPositionProvider.getName(), viewCoordinates.getPoint().getX() - size.getX() / 2.0, viewCoordinates.getPoint().getY() - 16);
            double eta = TimeUtils.getETA(orbitalPositionProvider);
            String etaTime = TimeUtils.periodAsString(eta);
            size = getTextSize(etaTime);
            drawString(etaTime, viewCoordinates.getPoint().getX() - size.getX() / 2.0, viewCoordinates.getPoint().getY() - 26);
        }
    }
}
