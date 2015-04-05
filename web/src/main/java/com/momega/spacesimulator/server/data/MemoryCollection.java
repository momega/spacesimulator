/**
 * 
 */
package com.momega.spacesimulator.server.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author martin
 *
 */
public class MemoryCollection<T> implements Collection<T> {

	private Map<String, T> list = new HashMap<>();
	private int index = 0;

	public String add(T item) {
		index++;
		String id = String.valueOf(index);
		list.put(id, item);
		return id;
	}
	
	public void update(String id, T item) {
		list.put(id, item);
	}
	
	public T get(String id) {
		return list.get(id);
	}
	
	public void remove(String id) {
		list.remove(id);
	}
	
	public Map<String, T> getAll() {
		return list;
	}
}
