package com.momega.spacesimulator.renderer;

import com.jogamp.common.nio.Buffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * Superclass for all bitmap renderers
 * Created by martin on 10/10/14.
 */
public abstract class AbstractBitmapRenderer extends AbstractRenderer {

    private static final Logger logger = LoggerFactory.getLogger(AbstractBitmapRenderer.class);

    protected int imgHeight;
    protected int imgWidth;
    protected ByteBuffer imgRGBA;

    public void init(GL2 gl) {
        loadBitmap(gl);
    }

    protected abstract void loadBitmap(GL2 gl);

    protected abstract Point getPoint();

    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        Point point = getPoint();
        if (point == null) {
            return;
        }

        gl.glPushAttrib(GL2.GL_DEPTH_BUFFER_BIT);
        gl.glPushAttrib(GL2.GL_COLOR_BUFFER_BIT);
        {
            gl.glDisable(GL2.GL_DEPTH_TEST);

            // enable alpha mask (import from gif sets alpha bits)
            gl.glEnable(GL2.GL_BLEND);
            gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

            setPosition(gl);
            gl.glPixelZoom(1f, 1f); // x-factor, y-factor
            gl.glBitmap (0, 0, 0, 0, -imgWidth/2, -imgHeight/2, null); // move the center of the bitmap
            gl.glDrawPixels(imgWidth, imgHeight, GL2.GL_RGBA, GL.GL_UNSIGNED_BYTE, imgRGBA);

        }
        gl.glPopAttrib();
        gl.glPopAttrib();
    }

    protected abstract void setPosition(GL2 gl);

    protected void loadBitmap(Class<?> clazz, String filename) {
        Graphics2D g = null;
        try {
            URL url = clazz.getResource(filename);
            Image img = Toolkit.getDefaultToolkit().createImage(url);
            MediaTracker tracker = new MediaTracker(new Canvas());
            tracker.addImage(img, 0);
            tracker.waitForAll(1000);

            imgHeight = img.getHeight(null);
            imgWidth = img.getWidth(null);
            logger.debug("Image, width={} height={}" + imgWidth, imgHeight);

            // Create a raster with correct size,
            // and a colorModel and finally a bufImg.
            //
            WritableRaster raster =
                    Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                            imgWidth,
                            imgHeight,
                            4,
                            null);
            ComponentColorModel colorModel =
                    new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                            new int[]{8, 8, 8, 8},
                            true,
                            false,
                            ComponentColorModel.TRANSLUCENT,
                            DataBuffer.TYPE_BYTE);
            BufferedImage bufImg =
                    new BufferedImage(colorModel, // color model
                            raster,
                            false, // isRasterPremultiplied
                            null); // properties

            // Filter img into bufImg and perform
            // Coordinate Transformations on the way.
            //
            g = bufImg.createGraphics();
            AffineTransform gt = new AffineTransform();
            gt.translate(0, imgHeight);
            gt.scale(1, -1d);
            g.transform(gt);
            g.drawImage(img, null, null);
            // Retrieve underlying byte array (imgBuf)
            // from bufImg.
            DataBufferByte imgBuf = (DataBufferByte) raster.getDataBuffer();
            this.imgRGBA = Buffers.newDirectByteBuffer(imgBuf.getData());
            //this.imgRGBA.flip();

        } catch (InterruptedException ie) {
            throw new IllegalStateException(ie);
        } finally {
            if (g != null) {
                g.dispose();
            }
        }
    }

}
