package com.momega.spacesimulator.model;

/**
 This class represents a 3d object. It is defined with the position as {@link com.momega.spacesimulator.model.Vector3d} and its {@link com.momega.spacesimulator.model.Orientation}.
 */
public class Object3d {

    private Vector3d position;
    private Orientation orientation;

    //################## CONSTRUCTORS ##################//

    /**
     * Returns the position of the object
     * @return the [x,y,z] coordinates
     */
    public Vector3d getPosition() {
        return position;
    }

    /**
     * Gets the orientation of the 3d object
     * @return
     */
    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
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