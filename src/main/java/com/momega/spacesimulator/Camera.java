package com.momega.spacesimulator;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 This class represents a camera for 3D views. Cameras are defined with
 a position and three mutually-orthogonal axes, namely n (points in the
 direction faced by the camera), u (points to the left of the camera)
 and v (points to the top of the camera).
 */
public class Camera
{

    final public static double EPSILON = 0.0001;

    private Vector3d position;
    private Vector3d nVector;
    private Vector3d uVector;
    private Vector3d vVector;

    //################## CONSTRUCTORS ##################//
    /**
     Constructs a new camera.

     @param position	The position of the camera
     @param look		The direction the camera is looking
     @param up		The "up" direction for the camera
     */
    public Camera(Vector3d position, Vector3d look, Vector3d up)
    {
        this.position = position;

        nVector = look;
        nVector.normalize();

        vVector = up;
        vVector.normalize();

        uVector = vVector.cross(nVector).normalize();
    }

    /**
     Returns the n vector of the camera.

     @return	...think about it...
     */
    public Vector3d getN()
    {
        return nVector;
    }

    /**
     Returns the position of the camera.

     @return	...think about it...
     */
    public Vector3d getPosition()
    {
        return position;
    }

    /**
     Returns the u vector of the camera.

     @return	...think about it...
     */
    public Vector3d getU()
    {
        return uVector;
    }

    /**
     Returns the v vector of the camera.

     @return	...think about it...
     */
    public Vector3d getV()
    {
        return vVector;
    }

    /**
     Moves the camera by the specified displacement in the n direction.

     @param delta	The displacement by which to move
     */
    public void moveN(double delta)
    {
        position = Vector3d.scaleAdd(delta, nVector, position);
    }

    /**
     Moves the camera by the specified displacement in the u direction.

     @param delta	The displacement by which to move
     */
    public void moveU(double delta)
    {
        position = Vector3d.scaleAdd(delta, uVector, position);
    }

    /**
     Moves the camera by the specified displacement in the v direction.

     @param delta	The displacement by which to move
     */
    public void moveV(double delta)
    {
        position = Vector3d.scaleAdd(delta, vVector, position);
    }

    /**
     Rotates the camera anticlockwise by the specified angle about the specified axis.

     @param axis		The axis about which to rotate
     @param angle	The angle by which to rotate (in radians)
     */
    public void rotate(Vector3d axis, double angle)
    {
        // Note: We try and optimise things a little by observing that there's no point rotating
        // an axis about itself and that generally when we rotate about an axis, we'll be passing
        // it in as the parameter axis, e.g. camera.rotate(camera.get_n(), Math.PI/2).
        if(axis != nVector) nVector = rotateAboutAxis(nVector, angle, axis);
        if(axis != uVector) uVector = rotateAboutAxis(uVector, angle, axis);
        if(axis != vVector) vVector = rotateAboutAxis(vVector, angle, axis);
    }

    /**
     Rotates vector v anticlockwise about the specified axis by the specified angle (in degrees).

     <p><b>Preconditions:</b>
     <dl>
     <dd>v != null
     <dd>axis != null
     <dd>axis must be normalized
     </dl>

     @param v			The vector to rotate about the axis
     @param degreeAngle	The angle by which to rotate it (in degrees)
     @param axis			The axis about which to rotate it
     @return				A (new) vector containing the result of the rotation
     @throws java.lang.Error	If any of the preconditions are not met
     */
    private static Vector3d rotateAboutAxis(final Vector3d v, final double degreeAngle, final Vector3d axis)
    {
        // Check the preconditions.
        if(v == null || axis == null) throw new java.lang.Error();
        if(Math.abs(axis.length() - 1) >= EPSILON) throw new java.lang.Error();	// make sure axis is normalized

        // Main algorithm
        double radianAngle = degreeAngle*Math.PI/180.0;
        double cosAngle = Math.cos(radianAngle), sinAngle = Math.sin(radianAngle);
        Vector3d aCROSSv = axis.cross(v);

        // ret = v cos radianAngle + (axis x v) sin radianAngle + axis(axis . v)(1 - cos radianAngle)
        // (See Mathematics for 3D Game Programming and Computer Graphics, P.62, for details of why this is (it's not very hard)).
        Vector3d ret = v.clone();
        ret.scale(cosAngle);
        ret = Vector3d.scaleAdd(sinAngle, aCROSSv, ret);
        ret = Vector3d.scaleAdd(axis.dot(v) * (1 - cosAngle), axis, ret);
        return ret;
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
       // gl.glLoadIdentity();
        glu.gluLookAt(	position.x, position.y, position.z,
                position.x + nVector.x, position.y + nVector.y, position.z + nVector.z,
                vVector.x, vVector.y, vVector.z);
    }
}