package com.momega.spacesimulator.model;

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

    /**
     * Returns the n vector of the object3d.
     */
    public Vector3d getN()
    {
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


}
