package com.momega.spacesimulator.model;

/**
 * Created by martin on 9.5.2014.
 */
public class Orientation {

    public final static double EPSILON = 0.0001;

    private Vector3d nVector;
    private Vector3d uVector;
    private Vector3d vVector;

    //################## CONSTRUCTORS ##################//
    /**
     Constructs a new 3d object.

     @param nVector		The direction the object3d is looking (or moving)
     @param vVector		The "up" direction for the object3d
     */
    public Orientation(Vector3d nVector, Vector3d vVector)
    {
        this.nVector = nVector.normalize();
        this.vVector = vVector.normalize();
        this.uVector = this.vVector.cross(this.nVector);
    }

    /**
     Returns the n vector of the object3d.

     @return	...think about it...
     */
    public Vector3d getN()
    {
        return nVector;
    }

    /**
     Returns the u vector of the object3d.

     @return	...think about it...
     */
    public Vector3d getU()
    {
        return uVector;
    }

    /**
     Returns the v vector of the object3d.

     @return	...think about it...
     */
    public Vector3d getV()
    {
        return vVector;
    }


    public void twist(double step) {
        rotate(getN(), step);
    }

    public void lookLeft(double step) {
        rotate(new Vector3d(0,0,1), step);
    }

    public void lookUp(double step) {
        rotate(getU(), step);
    }


    /**
     Rotates the object3d anticlockwise by the specified angle about the specified axis.

     @param axis		The axis about which to rotate
     @param angle	The angle by which to rotate (in radians)
     */
    public void rotate(Vector3d axis, double angle)
    {
        // Note: We try and optimise things a little by observing that there's no point rotating
        // an axis about itself and that generally when we rotate about an axis, we'll be passing
        // it in as the parameter axis, e.g. object3d.rotate(object3d.get_n(), Math.PI/2).
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
     @param angle	The angle by which to rotate it (in radians)
     @param axis			The axis about which to rotate it
     @return				A (new) vector containing the result of the rotation
     @throws java.lang.Error	If any of the preconditions are not met
     */
    private static Vector3d rotateAboutAxis(final Vector3d v, final double angle, final Vector3d axis)
    {
        // Check the preconditions.
        if(v == null || axis == null) throw new java.lang.Error();
        if(Math.abs(axis.length() - 1) >= EPSILON) {
            throw new IllegalStateException("axis is not normalized");	// make sure axis is normalized
        }

        // Main algorithm
        double cosAngle = Math.cos(angle), sinAngle = Math.sin(angle);
        Vector3d cross = axis.cross(v);

        // ret = v cos radianAngle + (axis x v) sin radianAngle + axis(axis . v)(1 - cos radianAngle)
        // (See Mathematics for 3D Game Programming and Computer Graphics, P.62, for details of why this is (it's not very hard)).
        Vector3d ret = v.clone();
        ret.scale(cosAngle);
        ret = Vector3d.scaleAdd(sinAngle, cross, ret);
        ret = Vector3d.scaleAdd(axis.dot(v) * (1 - cosAngle), axis, ret);
        return ret;
    }
}
