/**
 * 
 */
package com.momega.spacesimulator.renderer;

import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;

/**
 * @author martin
 *
 */
public class TransparencyImageFilter extends RGBImageFilter {
	
	private BufferedImage transparencyMapImage;

	/**
	 * 
	 */
	public TransparencyImageFilter(BufferedImage transparencyMapImage) {
		this.transparencyMapImage = transparencyMapImage;
	}

	@Override
	public int filterRGB(int x, int y, int rgb) {
		int t = transparencyMapImage.getRGB(x, 0);
		t = (t & 0xFF) << 24;
		int color = (rgb & 0xFFFFFF) | t;
		return color;
	}

}
