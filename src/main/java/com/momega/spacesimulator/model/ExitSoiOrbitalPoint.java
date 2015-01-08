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
	private Apsis closestPoint;
	
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
	
	public Apsis getClosestPoint() {
		return closestPoint;
	}
	
	public void setClosestPoint(Apsis closestPoint) {
		this.closestPoint = closestPoint;
	}

}
