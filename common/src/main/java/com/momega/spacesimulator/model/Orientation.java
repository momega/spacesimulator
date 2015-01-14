package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.VectorUtils;
import org.apache.commons.math3.util.FastMath;

/**
 * Orientation class is used to define 3D orientation. It is defined with
 * the three mutually-orthogonal axes, namely {@link #n} (points in the
 * direction faced by the object), {@link #u}  (points to the left of the object)
 * and {@link #v}  (points to the top of the object).
 * Created by martin on 9.5.2014.
 */
public class Orientation {

    private Vector3d n;
    private Vector3d u;
    private Vector3d v;

    public Orientation(Vector3d nVector, Vector3d vVector) {
        setN(nVector.normalize());
        setV(vVector.normalize());
        setU(getV().cross(getN()));
    }

    public static Orientation createUnit() {
        return new Orientation(Vector3d.ONE_X, Vector3d.ONE_Z);
    }

    /**
     * Returns the n vector of the object3d.
     */
    public Vector3d getN() {
        return n;
    }

    /**
     * Returns the u vector of the object3d.
     */
    public Vector3d getU()
    {
        return u;
    }

    /**
     * Returns the v vector of the object3d.
     */
    public Vector3d getV()
    {
        return v;
    }

    public void setN(Vector3d nVector) {
        this.n = nVector;
    }

    public void setU(Vector3d uVector) {
        this.u = uVector;
    }

    public void setV(Vector3d vVector) {
        this.v = vVector;
    }

    /**
     * Rotates the orientation up
     * @param step the angle in radians
     */
    public void lookUp(double step) {
        rotate(this.u, step);
    }

    public void lookLeft(double step) {
        rotate(new Vector3d(0,0,1), step);
    }

    public void lookAroundV(double step) {
        rotate(this.v, step);
    }

    public void twist(double step) {
        rotate(this.n, step);
    }

    public Orientation clone() {
        Orientation o = new Orientation(getN().clone(), getV().clone());
        return o;
    }

    /**
     * Rotates the orientation object anticlockwise by the specified angle about the specified axis.
     * @param axis	The axis about which to rotate
     * @param angle	The angle by which to rotate (in radians)
     */
    public void rotate(Vector3d axis, double angle)
    {
        // Note: We try and optimise things a little by observing that there's no point rotating
        // an axis about itself and that generally when we rotate about an axis, we'll be passing
        // it in as the parameter axis, e.g. object3d.rotate(object3d.getN(), Math.PI/2).
        if(axis != getN()) {
            setN(VectorUtils.rotateAboutAxis(getN(), angle, axis));
        }
        if(axis != getU()) {
            setU(VectorUtils.rotateAboutAxis(getU(), angle, axis));
        }
        if(axis != getV()) {
            setV(VectorUtils.rotateAboutAxis(getV(), angle, axis));
        }
    }

    /**
     * Gets the spherical coordinates defined by the vector and the orientation object (3 axis).
     * The radius parameter will be used only for radius in result spherical coordinates
     * @param point the given point
     * @param radius
     * @return
     */
    public SphericalCoordinates getCoordinatesOfVector(Vector3d point, double radius) {
        double theta = FastMath.abs(getV().angle(point));
        Plane xy = new Plane(Vector3d.ZERO, getV());
        Vector3d projectionV = xy.projection(point);
        double phi = getN().angle(projectionV);
        return new SphericalCoordinates(radius, theta, phi);
    }
}
