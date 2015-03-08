/**
 * 
 */
package com.momega.spacesimulator.server.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author martin
 * Note: The class will be replaced later with Mongo database
 */
public abstract class AbstractDatabase<T> {

	private Map<Integer, T> list = new HashMap<>();
	private int id = 0;

	public synchronized int add(T item) {
		id++;
		add(id, item);
		return id;
	}
	
	public synchronized void add(int id, T item) {
		list.put(Integer.valueOf(id), item);
	}
	
	public T get(int id) {
		Integer i = Integer.valueOf(id);
		return list.get(i);
	}
	
	public T remove(int id) {
		Integer i = Integer.valueOf(id);
		return list.remove(i);
	}
	
	public Map<Integer, T> getAll() {
		return list;
	}
}
