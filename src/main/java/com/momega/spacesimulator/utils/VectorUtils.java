package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.Orientation;
import com.momega.spacesimulator.model.Vector3d;

/**
 * Created by martin on 6/22/14.
 */
public class VectorUtils {

    private final static double EPSILON = 0.0001;

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
     @throws Error	If any of the preconditions are not met
     */
    public static Vector3d rotateAboutAxis(final Vector3d v, final double angle, final Vector3d axis)
    {
        // Check the preconditions.
        if(v == null || axis == null) {
            throw new IllegalArgumentException("axis or vector is null");
        }
        if(Math.abs(axis.length() - 1) >= EPSILON) {
            throw new IllegalStateException("axis is not normalized");	// make sure axis is normalized
        }

        // Main algorithm
        double cosAngle = Math.cos(angle);
        Vector3d cross = axis.cross(v);

        // ret = v cos radianAngle + (axis x v) sin radianAngle + axis(axis . v)(1 - cos radianAngle)
        // (See Mathematics for 3D Game Programming and Computer Graphics, P.62, for details of why this is (it's not very hard)).
        Vector3d ret = v.scale(cosAngle);
        ret = ret.scaleAdd(Math.sin(angle), cross);
        ret = ret.scaleAdd(axis.dot(v) * (1 - cosAngle), axis);
        return ret;
    }

    /**
     * Rotates the object3d anticlockwise by the specified angle about the specified axis.
     * @param  orientation the vector to be rotate
     * @param axis	The axis about which to rotate
     * @param angle	The angle by which to rotate (in radians)
     */
    public static void rotate(Orientation orientation, Vector3d axis, double angle)
    {
        // Note: We try and optimise things a little by observing that there's no point rotating
        // an axis about itself and that generally when we rotate about an axis, we'll be passing
        // it in as the parameter axis, e.g. object3d.rotate(object3d.get_n(), Math.PI/2).
        if(axis != orientation.getN()) {
            orientation.setN(rotateAboutAxis(orientation.getN(), angle, axis));
        }
        if(axis != orientation.getU()) {
            orientation.setU(rotateAboutAxis(orientation.getU(), angle, axis));
        }
        if(axis != orientation.getV()) {
            orientation.setV(rotateAboutAxis(orientation.getV(), angle, axis));
        }
    }

    /**
     * Rotates the orientation up
     * @param o the orientation
     * @param step the angle in radians
     */
    public static void lookUp(Orientation o, double step) {
        rotate(o, o.getU(), step);
    }

    public static void lookLeft(Orientation o, double step) {
        rotate(o, new Vector3d(0, 0, 1), step);
    }

    public static void twist(Orientation o, double step) {
        rotate(o, o.getN(), step);
    }
}
