/**
 * 
 */
package com.momega.spacesimulator.model;

/**
 * @author martin
 *
 */
public interface OrbitPositionProvider extends PositionProvider {

	/**
	 * Gets the true anomaly of the point. It may differ from the true anomaly stored
	 * in the {@link #getKeplerianElements()}. The example can be apsis or orbital plane intersection
	 * @return the angle in radians
	 */
	double getTrueAnomaly();
	
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
