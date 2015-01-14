/**
 * 
 */
package com.momega.spacesimulator.model;

/**
 * @author martin
 *
 */
public abstract class AbstractTargetOrbitalPoint extends AbstractOrbitalPoint {

	private transient MovingObject targetObject;

    public MovingObject getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(MovingObject targetObject) {
        this.targetObject = targetObject;
    }

}
