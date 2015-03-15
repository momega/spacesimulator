package com.momega.spacesimulator.server.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.service.HistoryPointService;
import com.momega.spacesimulator.service.ModelWorker;

@Component
public class ModelExecutor {
	
	private Map<Integer, FutureTask<Model>> futures = new HashMap<>();

	@Autowired
	private ModelWorker modelWorker;
	
	@Autowired
	private AsyncTaskExecutor taskExecutor;	
	
	@Autowired
	private HistoryPointService historyPointService;
	
	public ModelRunnable create(Model model) {
		ModelRunnable runnable = new ModelRunnable(modelWorker, model, 1.0, true);
		return runnable;
	}
 	
	public void start(int id, ModelRunnable runnable) {
		FutureTask<Model> task = new FutureTask<>(runnable);
		taskExecutor.submit(task);
		futures.put(Integer.valueOf(id), task);
	}
	
	public Model stop(int id, final ModelRunnable modelRunnable) {
		if (modelRunnable.isRunning()) {
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					modelRunnable.setRunning(false);
				}
			});
			try {
				FutureTask<Model> task = futures.remove(Integer.valueOf(id));
				Assert.notNull(task);
				return task.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new IllegalStateException("unable to stop the thread", e);
			}
		} else {
			return modelRunnable.getModel();
		}
	}

}
