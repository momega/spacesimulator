/**
 * 
 */
package com.momega.spacesimulator.model;

import com.momega.spacesimulator.context.ModelHolder;

/**
 * @author martin
 *
 */
public abstract class AbstractOrbitalPoint extends NamedObject implements PositionProvider {

    private Vector3d position;
    private Timestamp timestamp;
    private KeplerianElements keplerianElements;
    private boolean visible;
    private MovingObject movingObject;

    @Override
    public Vector3d getPosition() {
		return position;
	}

	public void setPosition(Vector3d position) {
		this.position = position;
	}

    @Override
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
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

    /**
     * Returns the moving object to which the orbital position provider belongs
     * @return
     */
	public MovingObject getMovingObject() {
		return movingObject;
	}
	
	public void setMovingObject(MovingObject movingObject) {
		this.movingObject = movingObject;
	}

}
