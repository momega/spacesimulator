package com.momega.spacesimulator;

import com.jogamp.common.util.IOUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import java.io.IOException;
import java.io.InputStream;

import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;

/**
 * The class represents the planet. It is the 3d object with texture and displayed as shpere
 * The planet is the sphere with the given radius
 *
 * Created by martin on 4/15/14.
 */
public class Planet extends Object3d {

    private Texture texture;
    private double fi = 0;
    private float radius;
    private String textureFileName;


    /**
     Constructs a new camera.

     @param position	The position of the camera
     @param nVector		The direction the camera is looking
     @param vVector		The "up" direction for the camera
     */
    public Planet(Vector3d position, Vector3d nVector, Vector3d vVector, float radius, String textureFileName) {
        super(position, nVector, vVector);
        this.radius = radius;
        this.textureFileName = textureFileName;
    }

    public void loadTexture(GL2 gl) {
        this.texture = loadTexture(gl, this.textureFileName);
    }

    private Texture loadTexture(GL2 gl, String fileName) {

        InputStream stream = null;
        try {
            stream = getClass().getResourceAsStream(fileName);
            TextureData data = TextureIO.newTextureData(GLProfile.getDefault(), stream, false, "jpg");
            Texture result = TextureIO.newTexture(data);
            result.setTexParameteri(gl, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            return result;
        }
        catch (IOException exc) {
            IOUtil.close(stream, true);
        }

        return null;
    }

    public void draw(GL2 gl, GLU glu) {
        texture.enable(gl);
        texture.bind(gl);
        gl.glPushMatrix();
        GLUquadric quadric = glu.gluNewQuadric();
        gl.glColor3f(1f, 1f, 1f);
        glu.gluQuadricTexture(quadric, true);
        gl.glTranslatef((float)position.x, (float)position.y, (float)position.z);
        gl.glRotated(this.fi * 180 / Math.PI, 0, 0, 1);
        glu.gluSphere(quadric, radius, 64, 64);
        glu.gluDeleteQuadric(quadric);
        gl.glPopMatrix();
    }

    public void rotate(float angle) {
        this.fi += angle;
    }

    public void dispose(GL2 gl) {
        if (texture != null) {
            texture.destroy(gl);
        }
    }

}
