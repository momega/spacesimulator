/**
 * 
 */
package com.momega.spacesimulator.model;

/**
 * @author martin
 *
 */
public interface OrbitalPositionProvider extends PositionProvider {

	/**
	 * Gets the {@link KeplerianElements} associated with the point.
	 * @return
	 */
	KeplerianElements getKeplerianElements();
	
	/**
	 * Returns whether the point is visible or not
	 * @return
	 */
	boolean isVisible();
	
	/**
	 * Sets whether the point is visible or not
	 * @param visible
	 */
	void setVisible(boolean visible);

	/**
	 * Returns associated object
	 * @return the object to which the position provider belongs
	 */
	MovingObject getMovingObject();
}
