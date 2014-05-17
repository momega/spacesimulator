package com.momega.spacesimulator.model;

/**
 * This class represents 3D vectors, storing their components as doubles.
 * Note that the class can be used to represent both point vectors and
 * free vectors. This is by design and is intended to make working with
 * vectors easier.
 */
public class Vector3d {

    final private static double SMALL_EPSILON = 0.0001;

    public double x, y, z;

    /**
     * Constructs a new Vector3d.
     */
    public Vector3d() {
    }

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

    //################## PUBLIC METHODS ##################//
        public Vector3d add(final Vector3d rhs) {
        x += rhs.x;
        y += rhs.y;
        z += rhs.z;
        return this;
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

    /**
     * Returns the result of the cross product lhs x rhs.
     *
     * @param lhs The left-hand operand of the cross product
     * @param rhs The right-hand operand of the cross product
     * @return The cross product of the two vectors as a Vector3d
     */
    public static Vector3d cross(final Vector3d lhs, final Vector3d rhs) {
        return lhs.cross(rhs);
    }

    public double distance(final Vector3d rhs) {
        return Math.sqrt(distanceSquared(rhs));
    }

    public double distanceSquared(final Vector3d rhs) {
        double dx = x - rhs.x, dy = y - rhs.y, dz = z - rhs.z;
        return dx * dx + dy * dy + dz * dz;
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

    public double lengthSquared() {
        return x * x + y * y + z * z; // note that dot(this); would do, but it's needlessly inefficient
    }

    public Vector3d negate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Vector3d negated() {
        return new Vector3d(-x, -y, -z);
    }

    public Vector3d normalize() {
        double len = length();
        if (Math.abs(len) < SMALL_EPSILON) {
            throw new IllegalStateException("length to small");
        }

        scale(1.0 / len);
        return this;
    }

    public Vector3d scale(double factor) {
        x *= factor;
        y *= factor;
        z *= factor;
        return this;
    }

    public Vector3d scaled(double factor) {
        return new Vector3d( x * factor, y * factor, z * factor);
    }

    /**
     * Returns the result of the operation factor*u + v.
     *
     * @param factor The scaling factor for u
     * @param u      The vector to scale
     * @param v      The vector to add
     * @return ...think about it...
     */
    public static Vector3d scaleAdd(double factor, final Vector3d u, final Vector3d v) {
        return new Vector3d(factor * u.x + v.x, factor * u.y + v.y, factor * u.z + v.z);
    }

    /**
     * Diff vector between 2 vectors (u-v)
     * @param u
     * @param v
     * @return
     */
    public static Vector3d diff(Vector3d u, Vector3d v) {
        return scaleAdd(-1d, v, u);
    }

    public static double angleBetween(Vector3d a, Vector3d b) {
        double cosAlpha = a.dot(b) / a.length() / b.length();
        if(cosAlpha > 1) cosAlpha = 1;
        return Math.acos(cosAlpha);
    }

    public Vector3d subtract(final Vector3d rhs) {
        x -= rhs.x;
        y -= rhs.y;
        z -= rhs.z;
        return this;
    }

    public double[] asArray() {
        return new double[] {x,y,z};
    }

    public String toString() {
        String result = String.format("( %6.3f, %6.3f, %6.3f)", x, y, z);
        return result;
    }

}