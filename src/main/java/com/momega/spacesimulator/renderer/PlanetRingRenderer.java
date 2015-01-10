package com.momega.spacesimulator.renderer;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Ring;
import com.momega.spacesimulator.model.SphericalCoordinates;
import com.momega.spacesimulator.opengl.GLUtils;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This renderer displays the ring of the planet Created by martin on 7/9/14.
 */
public class PlanetRingRenderer extends AbstractTextureRenderer {
	
	private static final Logger logger = LoggerFactory.getLogger(PlanetRingRenderer.class);

	private final CelestialBody celestialBody;
	private final Ring ring;

	protected PlanetRingRenderer(CelestialBody celestialBody, Ring ring) {
		this.celestialBody = celestialBody;
		this.ring = ring;
	}

	@Override
	protected Texture loadTexture(GL2 gl) {
		InputStream is = createTransparentStream(ring.getTextureFileName(),
				ring.getTransparencyFileName());
		Texture texture = GLUtils.loadTexture(gl, is, TextureIO.PNG , false);
		
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
		
		logger.info("texture ring {},{} is created for {}",
				ring.getTextureFileName(), ring.getTransparencyFileName(), celestialBody.getName());
		return texture;
	}

	@Override
	protected void drawTextObject(GL2 gl) {
		double max = ring.getMaxDistance();
		double min = ring.getMinDistance();
		GLUtils.drawRing(gl, min, max, 360, 36);
	}

	@Override
	protected void setMatrix(GL2 gl) {
		SphericalCoordinates sphericalCoordinates = new SphericalCoordinates(celestialBody.getOrientation().getV());
		GLUtils.translate(gl, celestialBody.getCartesianState().getPosition());
		GLUtils.rotate(gl, sphericalCoordinates);
	}

	protected InputStream createTransparentStream(String fileName,
			String transparencyFileName) {
		try {
			InputStream is = getClass().getResourceAsStream(fileName);
			BufferedImage source = ImageIO.read(is);

			InputStream tis = getClass().getResourceAsStream(transparencyFileName);
			BufferedImage transparency = ImageIO.read(tis);

			ImageProducer ip = new FilteredImageSource(source.getSource(), new TransparencyImageFilter(transparency));
			Image image = Toolkit.getDefaultToolkit().createImage(ip);
			BufferedImage im = imageToBufferedImage(image,
					image.getWidth(null), image.getHeight(null));

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(im, "png", os);
			InputStream fis = new ByteArrayInputStream(os.toByteArray());

			return fis;
		} catch (IOException io) {
			throw new IllegalStateException(io);
		}
	}

	private BufferedImage imageToBufferedImage(Image image, int width, int height) {
		BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g2 = dest.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return dest;
	}
}
