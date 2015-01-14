/**
 * 
 */
package com.momega.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author martin
 *
 */
public class MappedList<K, V> extends HashMap<K, List<V>> {

	private static final long serialVersionUID = 3226777990100485071L;
	
	public void add(K key, V value) {
		List<V> list = get(key);
		if (list == null) {
			list = new ArrayList<V>();
			put(key, list);
		}
		list.add(value);
	}

    public List<V> getValues(K key) {
        List<V> list = get(key);
        if (list == null) {
            list = new ArrayList<V>();
            put(key, list);
        }
        return list;
    }
	
	public List<V> getAll() {
		List<V> list = new ArrayList<V>();
		for(Map.Entry<K, List<V>> entry : entrySet()) {
			list.addAll(entry.getValue());
		}
		return list;
	}
	
	public K findKey(V value) {
		for(Map.Entry<K, List<V>> entry : entrySet()) {
			if (entry.getValue().contains(value)) {
				return entry.getKey();
			}
		}
		return null;
	}
	
}
