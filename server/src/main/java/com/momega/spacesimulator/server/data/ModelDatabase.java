/**
 * 
 */
package com.momega.spacesimulator.server.data;

import java.util.HashMap;
import java.util.Map;

import com.momega.spacesimulator.model.Model;

/**
 * @author martin
 * Note: The class will be replaced later with Mongo database
 */
public class ModelDatabase {

	private Map<Integer, Model> models = new HashMap<Integer, Model>();
	private int id = 0;
	
	public int add(Model model) {
		id++;
		models.put(Integer.valueOf(id), model);
		return id;
	}
	
	public Model getModel(int id) {
		Integer i = Integer.valueOf(id);
		return models.get(i);
	}
	
	public void remove(int id) {
		Integer i = Integer.valueOf(id);
		models.remove(i);
	}
}
