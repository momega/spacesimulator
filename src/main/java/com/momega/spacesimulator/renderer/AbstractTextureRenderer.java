package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.jogamp.opengl.util.texture.Texture;
import com.momega.spacesimulator.opengl.GLUtils;

/**
 * Abstract renderer with prepares and uses the texture
 * Created by martin on 7/9/14.
 */
public abstract class AbstractTextureRenderer extends AbstractRenderer {

    protected Texture texture;
    protected int listIndex;

    protected Texture loadTexture(GL2 gl, String textureFileName) {
        this.texture =  GLUtils.loadTexture(gl, getClass(), textureFileName);
        return this.texture;
    }

    protected abstract void loadTexture(GL2 gl);

    @Override
    public void reload(GL2 gl) {
        dispose(gl);
        init(gl);
    }

    public void dispose(GL2 gl) {
        gl.glDeleteLists(this.listIndex, 1);
        if (texture != null) {
            texture.destroy(gl);
        }
        super.dispose(gl);
    }

    public void init(GL2 gl) {
        super.init(gl);
        this.listIndex = gl.glGenLists(1);
        if (this.listIndex==0) {
            throw new IllegalStateException("gl list not created");
        }
        loadTexture(gl);
        gl.glNewList(this.listIndex, GL2.GL_COMPILE);
        prepareObject(gl);
        gl.glEndList();
    }

    protected abstract void drawTextObject(GL2 gl);

    /**
     * Prepares the texture during initialization of the renderer
     * @param gl the OPENGL context
     */
    protected void prepareObject(GL2 gl) {
        texture.enable(gl);
        texture.bind(gl);
        drawTextObject(gl);
        texture.disable(gl);
    }

    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();

        setMatrix(gl);

        gl.glCallList(this.listIndex);
        additionalDraw(gl);
        
        gl.glPopMatrix();
    }

    protected void additionalDraw(GL2 gl) {
        // do nothing, reader for override
    }

    protected abstract void setMatrix(GL2 gl);

}
