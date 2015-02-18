/**
 * 
 */
package com.momega.spacesimulator.json;

import java.util.HashMap;
import java.util.Map;

import com.momega.spacesimulator.model.NamedObject;

/**
 * @author martin
 * NOTE: The class is not thread safe
 */
public class NamedObjectCache {

	private static NamedObjectCache instance = new NamedObjectCache();

	private Map<String, NamedObject> map = new HashMap<>();
	
	public static NamedObjectCache getInstance() {
		return instance;
	}
	
	private NamedObjectCache() {
		super();
	}
	
	public void add(NamedObject object) {
		map.put(object.getName(), object);
	}
	
	public NamedObject get(String name) {
		return map.get(name);
	}
	
	public void clear() {
		map.clear();
	}

}
