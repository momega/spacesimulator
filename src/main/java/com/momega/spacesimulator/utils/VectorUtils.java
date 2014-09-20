package com.momega.spacesimulator.utils;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.springframework.util.Assert;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Orientation;
import com.momega.spacesimulator.model.Vector3d;

/**
 * The class contains set of the function to manipulate with the vectors
 * Created by martin on 6/22/14.
 */
public final class VectorUtils {

    public final static double SMALL_EPSILON = 0.0001;
    
    public static Vector3d transform(KeplerianElements keplerianElements, Vector3d vector) {
    	Rotation r = new Rotation(RotationOrder.ZXZ, keplerianElements.getAscendingNode(), keplerianElements.getInclination(), keplerianElements.getArgumentOfPeriapsis());
    	Vector3D v = vector.asVector3D();
    	Vector3D rv = r.applyInverseTo(v);
    	Vector3d result = Vector3d.fromVector3D(rv);
    	return result;
    }
    
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
        double theta = Math.acos(vector.getZ() / length);
        double phi = Math.atan2(vector.getY(), vector.getX());
        return new double[] {length, theta, phi};
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
        return movingObject.getCartesianState().subtract(movingObject.getKeplerianElements().getCentralObject().getCartesianState());
    }

    public static boolean equals(Vector3d v1, Vector3d v2, double precision) {
        return v1.subtract(v2).length()<precision;
    }

    /**
     * Rotates vector v anticlockwise about the specified axis by the specified angle (in radians).
     * @param v			The vector to rotate about the axis
     * @param angle	The angle by which to rotate it (in radians)
     * @param axis			The axis about which to rotate it
     * @return				A (new) vector containing the result of the rotation
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

    public static Orientation rotateByAngles(Orientation o, double alpha, double delta, boolean toEcliptic) {
    	o.lookAroundV(alpha);
        o.lookUp(Math.PI / 2 - delta);
        if (toEcliptic) {
            o.rotate(new Vector3d(1, 0, 0), -VectorUtils.ECLIPTIC);
        }
        return o;
    }

    /**
     * Creates the rotation transformation
     * @param alpha right ascension
     * @param delta declination of the north-pole
     * @return the transformation matrix
     */
    public static Orientation createOrientation(double alpha, double delta, boolean toEcliptic) {
        Orientation o = createOrientation(new Vector3d(1, 0, 0), new Vector3d(0, 0, 1));
        rotateByAngles(o, alpha, delta, toEcliptic);
        return o;
    }

    public static Orientation createOrientation(Vector3d nVector, Vector3d vVector) {
        Orientation o = new Orientation();
        o.setN(nVector.normalize());
        o.setV(vVector.normalize());
        o.setU(o.getV().cross(o.getN()));
        return o;
    }

	/**
	 * Get the angle of the ecliptic
	 */
	public static final double ECLIPTIC = Math.toRadians(23.439291);
}
