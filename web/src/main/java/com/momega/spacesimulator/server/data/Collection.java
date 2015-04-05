/**
 * 
 */
package com.momega.spacesimulator.server.data;

import java.util.Map;

/**
 * @author martin
 */
public interface Collection<T> {

	public String add(T item);
	
	public void update(String id, T item);
	
	public T get(String id);
	
	public void remove(String id);
	
	public Map<String, T> getAll();
}
