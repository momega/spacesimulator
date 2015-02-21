/**
 * 
 */
package com.momega.spacesimulator.service;

import java.util.concurrent.atomic.AtomicBoolean;

import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.RunStep;

/**
 * @author martin
 *
 */
public class ModelRunnable implements Runnable {

	private ModelWorker worker;
	private Model model;
	private AtomicBoolean running = new AtomicBoolean(true);
	private RunStep runStep;

	public ModelRunnable(ModelWorker worker, Model model, double dt, boolean runningHeadless) {
		this.worker = worker;
		this.model = model;
		this.runStep = RunStep.create(model.getTime(), dt, runningHeadless);
	}

	@Override
	public void run() {
		while(running.get()) {
			worker.next(model, this.runStep);
			this.runStep.next();
		}
	}
	
	public void setRunning(boolean running) {
		this.running.set(running);
	}
	
	public Model getModel() {
		return model;
	}

}
