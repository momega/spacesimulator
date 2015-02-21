/**
 * 
 */
package com.momega.spacesimulator.server.data;

import java.util.HashMap;
import java.util.Map;

import com.momega.spacesimulator.service.ModelRunnable;

/**
 * @author martin
 * Note: The class will be replaced later with Mongo database
 */
public class ModelDatabase {

	private Map<Integer, ModelRunnable> models = new HashMap<Integer, ModelRunnable>();
	private int id = 0;
	
	public int add(ModelRunnable runnable) {
		id++;
		models.put(Integer.valueOf(id), runnable);
		return id;
	}
	
	public ModelRunnable getModel(int id) {
		Integer i = Integer.valueOf(id);
		return models.get(i);
	}
	
	public ModelRunnable remove(int id) {
		Integer i = Integer.valueOf(id);
		return models.remove(i);
	}
	
	public Map<Integer, ModelRunnable> getModels() {
		return models;
	}
}
