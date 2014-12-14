package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.VectorUtils;

/**
 * This class represents 3D vectors, storing their components as doubles.
 * Note that the class can be used to represent both point vectors and
 * free vectors. This is by design and is intended to make working with
 * vectors easier.
 *
 */
public class Vector3d {

    private final double x;
    private final double y;
    private final double z;

    public static final Vector3d ZERO = new Vector3d(0d, 0d, 0d);

    public static final Vector3d ONE_X = new Vector3d(1d, 0d, 0d);

    public static final Vector3d ONE_Y = new Vector3d(0d, 1d, 0d);

    public static final Vector3d ONE_Z = new Vector3d(0d, 0d, 1d);

    /**
     * Constructs a new Vector3d with the specified x, y and z components.
     *
     * @param x The x component of the new vector
     * @param y The y component of the new vector
     * @param z The z component of the new vector
     */
    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Adds two vectors. The operation is: result = this + u
     * @param u the second vector
     * @return new instance of the vector
     */
    public Vector3d add(final Vector3d u) {
        return scaleAdd(1, u);
    }

    public Vector3d scaleAdd(double factor, Vector3d u) {
        return new Vector3d(x + factor * u.x, y + factor * u.y, z + factor * u.z);
    }

    public Vector3d clone() {
        return new Vector3d(x, y, z);
    }

    /**
     * Returns the cross product of this vector and the parameter vector.
     *
     * @param rhs The right-hand operand of the cross product
     * @return The cross product of the two vectors as a Vector3d
     */
    public Vector3d cross(final Vector3d rhs) {
        return new Vector3d(y * rhs.z - z * rhs.y, z * rhs.x - x * rhs.z, x * rhs.y - y * rhs.x);
    }

    public double dot(final Vector3d rhs) {
        return x * rhs.x + y * rhs.y + z * rhs.z;
    }

    public boolean equals(final Vector3d rhs) {
        return (x == rhs.x) && (y == rhs.y) && (z == rhs.z);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);    // note that the squared length has been put inline for efficiency
    }
    
    public double angle(Vector3d vector) {
    	double cosAlpha = dot(vector) / length() / vector.length();
    	if(cosAlpha > 1) {
            cosAlpha = 1;
        }
    	double result = Math.acos(cosAlpha);
    	double sign = Math.signum(this.cross(vector).z);
    	return result*sign;
    }

    public double lengthSquared() {
        return x * x + y * y + z * z; // note that dot(this); would do, but it's needlessly inefficient
    }

     public Vector3d negate() {
        return new Vector3d(-x, -y, -z);
    }

    public Vector3d normalize() {
        double len = length();
        if (Math.abs(len) < VectorUtils.SMALL_EPSILON) {
            throw new IllegalStateException("length to small");
        }

        return scale(1.0 / len);
    }

    public Vector3d scale(double factor) {
        return new Vector3d( x * factor, y * factor, z * factor);
    }

    public Vector3d subtract(final Vector3d u) {
        return new Vector3d( x - u.x, y - u.y, z - u.z);
    }

    public double[] asArray() {
        return new double[] {x,y,z};
    }

    public String toString() {
        String result = String.format("(%6.2f, %6.2f, %6.2f)", x, y, z);
        return result;
    }

    public SphericalCoordinates toSphericalCoordinates() {
        return new SphericalCoordinates(this);
    }
    
    public double getX() {
		return x;
	}
    
    public double getY() {
		return y;
	}
    
    public double getZ() {
		return z;
	}

}