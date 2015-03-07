/**
 * 
 */
package com.momega.spacesimulator.server.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.RunStep;
import com.momega.spacesimulator.service.HistoryPointListener;
import com.momega.spacesimulator.service.ModelWorker;

/**
 * @author martin
 *
 */
public class ModelRunnable implements Callable<Model>, HistoryPointListener {
	
	private static final Logger logger = LoggerFactory.getLogger(ModelRunnable.class);

	private ModelWorker worker;
	private Model model;
	private AtomicBoolean running = new AtomicBoolean(true);
	private RunStep runStep;
	private List<HistoryPoint> historyPoints = new ArrayList<>();

	public ModelRunnable(ModelWorker worker, Model model, double dt, boolean runningHeadless) {
		this.worker = worker;
		this.model = model;
		this.runStep = RunStep.create(model.getTime(), dt, runningHeadless);
	}
	
	public void setRunning(boolean running) {
		this.running.set(running);
	}
	
	public Model getModel() {
		return model;
	}

	@Override
	public Model call() throws Exception {
		logger.info("start the thread");
		while(running.get()) {
			worker.next(model, this.runStep);
			this.runStep.next();
		}
		logger.info("thread about stop, execute last step ");
		// perform last step not headless
		this.runStep.setRunningHeadless(false);
		worker.next(model, this.runStep);
		return model;
	}
	
	public boolean isRunning() {
		return running.get();
	}

	@Override
	public void historyPointCreated(HistoryPoint historyPoint) {
		synchronized (historyPoints) {
			this.historyPoints.add(historyPoint);
		}
	}
	
	public void addHistoryPoints(List<HistoryPoint> list) {
		synchronized (list) {
			historyPoints.addAll(list);
		}
	}
	
	public List<HistoryPoint> getHistoryPoints() {
		synchronized (historyPoints) {
			return historyPoints;
		}
	}
	
	public HistoryPoint getLastHistoryPoint() {
		synchronized (historyPoints) {
			if (!historyPoints.isEmpty()) {
				return historyPoints.get(historyPoints.size()-1);
			} else {
				return null;
			}
		}
	}

}
