/**
 * 
 */
package com.momega.spacesimulator.model;


/**
 * @author martin
 *
 */
public class RunStep {
	
	private Timestamp newTimestamp;
	private double dt;
	private boolean runningHeadless;
	
	public static RunStep create(Timestamp currentTimestamp, double dt, boolean runningHeadless) {
		RunStep runStep = new RunStep();
		runStep.setDt(dt);
		runStep.setRunningHeadless(runningHeadless);
		runStep.setNewTimestamp(currentTimestamp.add(dt));
		return runStep;
	}
	
	public Timestamp getNewTimestamp() {
		return newTimestamp;
	}
	
	public void setNewTimestamp(Timestamp newTimestamp) {
		this.newTimestamp = newTimestamp;
	}
	
	public double getDt() {
		return dt;
	}
	
	public void setDt(double dt) {
		this.dt = dt;
	}
	
	public boolean isRunningHeadless() {
		return runningHeadless;
	}
	
	public void setRunningHeadless(boolean runningHeadless) {
		this.runningHeadless = runningHeadless;
	}
	
	public void next() {
		setNewTimestamp(getNewTimestamp().add(dt));
	}

}
