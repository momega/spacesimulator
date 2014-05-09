package com.momega.spacesimulator.model;

/**
 This class represents a 3d object. It is defined with
 a position and three mutually-orthogonal axes, namely n (points in the
 direction faced by the object), u (points to the left of the object)
 and v (points to the top of the object).
 */
public class Object3d {

    private Vector3d position;
    private Orientation orientation;

    //################## CONSTRUCTORS ##################//
    /**
     Constructs a new 3d object.
     @param position	The position of the object3d
     @param orientation		The orientation of the objects
     */
    public Object3d(Vector3d position, Orientation orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    /**
     Returns the position of the object3d.

     @return	...think about it...
     */
    public Vector3d getPosition()
    {
        return position;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    /**
     Moves the object3d by the specified displacement in the n direction.

     @param delta	The displacement by which to move
     */
    public void moveN(double delta)
    {
        position = Vector3d.scaleAdd(delta, orientation.getN(), position);
    }

    /**
     Moves the object3d by the specified displacement in the u direction.

     @param delta	The displacement by which to move
     */
    public void moveU(double delta)
    {
        position = Vector3d.scaleAdd(delta, orientation.getU(), position);
    }

    /**
     Moves the object3d by the specified displacement in the v direction.

     @param delta	The displacement by which to move
     */
    public void moveV(double delta)
    {
        position = Vector3d.scaleAdd(delta, orientation.getV(), position);
    }


}