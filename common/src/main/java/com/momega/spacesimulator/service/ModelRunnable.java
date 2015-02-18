/**
 * 
 */
package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.RunStep;

/**
 * @author martin
 *
 */
public class ModelRunnable implements Runnable {

	private ModelWorker worker;
	private Model model;
	private boolean running;
	private RunStep runStep;

	public ModelRunnable(ModelWorker worker, Model model, double dt, boolean runningHeadless) {
		this.worker = worker;
		this.model = model;
		this.runStep = RunStep.create(model.getTime(), dt, runningHeadless);
		this.running = true;
	}

	@Override
	public void run() {
		while(running) {
			worker.next(model, this.runStep);
			this.runStep.next();
		}
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}

}
