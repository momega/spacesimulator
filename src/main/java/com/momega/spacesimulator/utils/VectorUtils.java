package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Vector3d;

/**
 * Created by martin on 6/22/14.
 */
public class VectorUtils {

    private final static double EPSILON = 0.0001;
    public final static double SMALL_EPSILON = 0.0001;

    public static Vector3d fromSphericalCoordinates(double r, double theta, double phi) {
        return new Vector3d(r * Math.sin(theta)* Math.cos(phi), r*Math.sin(theta) * Math.sin(phi), r * Math.cos(theta));
    }

    public static Vector3d[] transformCoordinateSystem(MovingObject source, MovingObject target, Vector3d[] vectors) {
        Vector3d position = vectors[0].add(source.getPosition()).subtract(target.getPosition());
        Vector3d velocity = vectors[1].add(source.getVelocity()).subtract(target.getVelocity());
        return new Vector3d[] {position, velocity};
    }


    /**
     * Rotates vector v anticlockwise about the specified axis by the specified angle (in degrees).
     *
     * <p><b>Preconditions:</b>
     * <dl>
     * <dd>v != null
     * <dd>axis != null
     * <dd>axis must be normalized
     * </dl>
     *
     * @param v			The vector to rotate about the axis
     * @param angle	The angle by which to rotate it (in radians)
     * @param axis			The axis about which to rotate it
     * @return				A (new) vector containing the result of the rotation
     * @throws Error	If any of the preconditions are not met
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

    public static double angleBetween(Vector3d a, Vector3d b) {
        double cosAlpha = a.dot(b) / a.length() / b.length();
        if(cosAlpha > 1) cosAlpha = 1;
        return Math.acos(cosAlpha);
    }
}
