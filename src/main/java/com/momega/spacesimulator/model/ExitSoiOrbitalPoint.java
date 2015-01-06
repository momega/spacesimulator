/**
 * 
 */
package com.momega.spacesimulator.model;

/**
 * @author martin
 *
 */
public class ExitSoiOrbitalPoint extends AbstractTargetOrbitalPoint {

	private KeplerianElements predictedKeplerianElements;
	private double error;
	
	public KeplerianElements getPredictedKeplerianElements() {
		return predictedKeplerianElements;
	}
	
	public void setPredictedKeplerianElements(KeplerianElements predictedKeplerianElements) {
		this.predictedKeplerianElements = predictedKeplerianElements;
	}
	
	public double getError() {
		return error;
	}
	
	public void setError(double error) {
		this.error = error;
	}

}
