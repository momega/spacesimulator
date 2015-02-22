/**
 * 
 */
package com.momega.spacesimulator.server.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * @author martin
 * Note: The class will be replaced later with Mongo database
 */
@Component
public class ModelDatabase {

	private Map<Integer, ModelRunnable> models = new HashMap<>();
	private int id = 0;

	public int add(ModelRunnable runnable) {
		id++;
		add(id, runnable);
		return id;
	}
	
	public void add(int id, ModelRunnable runnable) {
		models.put(Integer.valueOf(id), runnable);
	}
	
	public ModelRunnable get(int id) {
		Integer i = Integer.valueOf(id);
		return models.get(i);
	}
	
	public ModelRunnable remove(int id) {
		Integer i = Integer.valueOf(id);
		return models.remove(i);
	}
	
	public Map<Integer, ModelRunnable> getAll() {
		return models;
	}
}
