/**
 * 
 */
package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.BaryCentre;
import com.momega.spacesimulator.model.MovingObject;

/**
 * @author martin
 *
 */
public class EmptyModelBuilder extends AbstractModelBuilder {

	private BaryCentre empty;

	@Override
	protected void initPlanets() {
		empty = new BaryCentre();
        updateDynamicalPoint(empty, "", 0, 0, 1, 0, null, null);
        setCentralPoint(empty);
        createTrajectory(empty, new double[] {1, 1, 1});
        addMovingObject(empty);
	}

	@Override
	protected void initCamera() {
		 createCamera(empty);
	}

	@Override
	protected void initSpacecrafts() {
		// do nothing
	}

	@Override
	protected MovingObject getCentralObject() {
		return empty;
	}

	@Override
	public String getName() {
		return "Empty model";
	}

}
