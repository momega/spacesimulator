/**
 * 
 */
package com.momega.spacesimulator.model;

import com.momega.spacesimulator.context.ModelHolder;

/**
 * @author martin
 *
 */
public abstract class AbstractOrbitalPoint extends NamedObject implements OrbitalPositionProvider {

    private Vector3d position;
    private Timestamp timestamp;
    private KeplerianElements keplerianElements;
    private boolean visible;
    private MovingObject movingObject;
	
    public Vector3d getPosition() {
		return position;
	}
	public void setPosition(Vector3d position) {
		this.position = position;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public KeplerianElements getKeplerianElements() {
		return keplerianElements;
	}
	public void setKeplerianElements(KeplerianElements keplerianElements) {
		this.keplerianElements = keplerianElements;
	}
	
	@Override
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public MovingObject getMovingObject() {
		return movingObject;
	}
	
	public void setMovingObject(MovingObject movingObject) {
		this.movingObject = movingObject;
	}

    /**
     * Returns ETA time in seconds between current time and planned time of the orbital point
     * @return the ETA in seconds
     */
    public double getETA() {
    	Timestamp current = ModelHolder.getModel().getTime();
    	Timestamp future = getTimestamp();
    	return future.subtract(current).doubleValue();
    }
}
