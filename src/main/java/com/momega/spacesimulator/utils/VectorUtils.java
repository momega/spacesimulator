package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.*;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.springframework.util.Assert;

/**
 * The class contains set of the function to manipulate with the vectors
 * Created by martin on 6/22/14.
 */
public final class VectorUtils {

    public final static double SMALL_EPSILON = 0.0001;
    
    public static Vector3d transform(KeplerianOrbit keplerianOrbit, Vector3d vector) {
    	Rotation r = new Rotation(RotationOrder.ZXZ, keplerianOrbit.getAscendingNode(), keplerianOrbit.getInclination(), keplerianOrbit.getArgumentOfPeriapsis());
    	Vector3D v = toVector3D(vector);
    	Vector3D rv = r.applyInverseTo(v);
    	Vector3d result = new Vector3d(rv.getX(), rv.getY(), rv.getZ());
    	return result;
    }

    public static CartesianState transformCoordinateSystem(MovingObject source, MovingObject target, CartesianState cartesianState) {
        CartesianState result = cartesianState.add(source.getCartesianState()).subtract(target.getCartesianState());
        return result;
    }

    /**
     * Gets the relative cartesian state to the current center of the orbit
     * @param movingObject the moving object (typically spacecraft)
     * @return the new instance of the cartesian state
     */
    public static CartesianState relativeCartesianState(MovingObject movingObject) {
        return movingObject.getCartesianState().subtract(movingObject.getKeplerianElements().getKeplerianOrbit().getCentralObject().getCartesianState());
    }

    public static boolean equals(Vector3d v1, Vector3d v2, double precision) {
        return v1.subtract(v2).length()<precision;
    }

    /**
     * Rotates vector v anticlockwise about the specified axis by the specified angle (in radians).
     * @param v	The vector to rotate about the axis
     * @param angle	The angle by which to rotate it (in radians)
     * @param axis	The axis about which to rotate it
     * @return	A (new) vector containing the result of the rotation
     */
    public static Vector3d rotateAboutAxis(final Vector3d v, final double angle, final Vector3d axis) {
        Assert.notNull(v);
        Assert.notNull(axis);
        Assert.isTrue(Math.abs(axis.length() - 1) < SMALL_EPSILON);

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

    public static Orientation rotateByAngles(Orientation o, double alpha, double delta) {
    	Orientation result = o.clone();
    	result.lookAroundV(alpha);
    	result.lookUp(delta);
    	return result;
    }

    /**
     * Creates the rotation transformation
     * @param alpha right ascension
     * @param delta declination of the north-pole
     * @return the transformation matrix
     */
    public static Orientation createOrientation(double alpha, double delta, boolean toEcliptic) {
        Orientation o = Orientation.createUnit();
        o.lookAroundV(alpha);
        o.lookUp(Math.PI / 2 - delta);
        if (toEcliptic) {
            o.rotate(new Vector3d(1, 0, 0), -VectorUtils.ECLIPTIC);
        }
        return o;
    }

    public static Vector3D toVector3D(Vector3d v) {
        return new Vector3D(v.getX(), v.getY(),v.getZ());
    }

	/**
	 * Get the angle of the ecliptic
	 */
	public static final double ECLIPTIC = Math.toRadians(23.439291);
}
