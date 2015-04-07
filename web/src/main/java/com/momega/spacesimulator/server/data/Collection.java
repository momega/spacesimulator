/**
 * 
 */
package com.momega.spacesimulator.server.data;

import java.util.Map;

/**
 * @author martin
 */
public interface Collection<T> {
	
	String add(T item);
	
	void update(String id, T item);
	
	T get(String id);
	
	long size();
	
	void remove(String id);
	
	Map<String, T> getAll();
}
