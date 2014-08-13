package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by martin on 6/22/14.
 */
public class VectorUtils {

    private static final Logger logger = LoggerFactory.getLogger(VectorUtils.class);

    private final static double EPSILON = 0.0001;
    public final static double SMALL_EPSILON = 0.0001;

    /**
     * Creates the vector from spherical coordinates
     * @param r distance
     * @param theta the angle from the z-axis
     * @param phi the angle from the x-axis
     * @return new instance of the vector
     *
     * @link http://en.wikipedia.org/wiki/Spherical_coordinate_system
     */
    public static Vector3d fromSphericalCoordinates(double r, double theta, double phi) {
        return new Vector3d(r * Math.sin(theta)* Math.cos(phi),
                            r * Math.sin(theta) * Math.sin(phi),
                            r * Math.cos(theta));
    }

    public static double[] toSphericalCoordinates(Vector3d vector) {
        double length = vector.length();
        double theta = Math.acos(vector.z / length);
        double phi = Math.atan2(vector.y, vector.x);
        return new double[] {length, theta, phi};
    }

    public static CartesianState transformCoordinateSystem(MovingObject source, MovingObject target, CartesianState cartesianState) {
        CartesianState result = cartesianState.add(source.getCartesianState()).subtract(target.getCartesianState());
        return result;
    }

    public static boolean equals(Vector3d v1, Vector3d v2, double precision) {
        return v1.subtract(v2).length()<precision;
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
    public static Vector3d rotateAboutAxis(final Vector3d v, final double angle, final Vector3d axis) {
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
        if(cosAlpha > 1) {
            cosAlpha = 1;
        }
        return Math.acos(cosAlpha);
    }

}
