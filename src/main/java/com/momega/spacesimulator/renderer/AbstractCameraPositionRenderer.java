/**
 * 
 */
package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;

import com.momega.spacesimulator.model.Camera;

/**
 * @author martin
 *
 */
public abstract class AbstractCameraPositionRenderer extends AbstractTextRenderer {

	protected AbstractCameraPositionRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.momega.spacesimulator.renderer.AbstractTextRenderer#renderTexts(javax.media.opengl.GL2, int, int)
	 */
	@Override
	protected void renderTexts(GL2 gl, int width, int height) {
		setColor(255, 255, 255);
        Camera c = getCamera();
        drawString("Distance:" + c.getDistance(), 10, 50);
        drawString("Position:" + c.getPosition().toString(), 10, 40);
        drawString("N:" + c.getOppositeOrientation().getN().toString(), 10, 30);
        drawString("U:" + c.getOppositeOrientation().getU().toString(), 10, 20);
        drawString("V:" + c.getOppositeOrientation().getV().toString(), 10, 10);
	}
	
	protected abstract Camera getCamera();

}
