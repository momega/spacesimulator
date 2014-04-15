package com.momega.spacesimulator;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * This class represents the camera. It is subclass od the 3d object
 *
 * Created by martin on 4/15/14.
 */
public class Camera extends Object3d {

    /**
     Constructs a new camera.

     @param position	The position of the camera
     @param nVector		The direction the camera is looking
     @param vVector		The "up" direction for the camera
     */
    public Camera(Vector3d position, Vector3d nVector, Vector3d vVector)
    {
        super(position, nVector, vVector);
    }

    /**
     Changes the view to be that of the camera.

     <p><b>Preconditions:</b>
     <dl>
     <dd>The matrix-mode of the GL context passed in must be MODELVIEW
     </dl>

     @param gl	The OpenGL object associated with the context in which to set the view
     @param glu	The GLU object associated with the context in which to set the view
     */
    public void setView(GL2 gl, GLU glu)
    {
        gl.glLoadIdentity();
        glu.gluLookAt(	position.x, position.y, position.z,
                position.x + nVector.x, position.y + nVector.y, position.z + nVector.z,
                vVector.x, vVector.y, vVector.z);
    }

}
