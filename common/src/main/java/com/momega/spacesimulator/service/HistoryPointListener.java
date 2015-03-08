/**
 * 
 */
package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.HistoryPoint;

/**
 * @author martin
 *
 */
public interface HistoryPointListener {

	/**
	 * The method is called whenever the history point is created
	 * @param historyPoint the history point
	 */
	public void historyPointCreated(HistoryPoint historyPoint);

	/**
	 * Returns true whether the listener has to be notified about the new point 
	 * @param historyPoint the instance of the new point
	 * @return the boolean value
	 */
	public boolean supports(HistoryPoint historyPoint);
}
