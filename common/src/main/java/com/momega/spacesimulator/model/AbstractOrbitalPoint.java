/**
 * 
 */
package com.momega.spacesimulator.model;


/**
 * The class is the superclass for all point associated with the orbit
 * @author martin
 */
public abstract class AbstractOrbitalPoint extends NamedObject implements PositionProvider, IconProvider {

    private Vector3d position;
    private Timestamp timestamp;
    private KeplerianElements keplerianElements;
    private boolean visible;
    private transient MovingObject movingObject;

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
	
	public void setMovingObject(MovingObject movingObject) {
		this.movingObject = movingObject;
	}
	
	public MovingObject getMovingObject() {
		return movingObject;
	}

}
