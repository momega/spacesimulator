/**
 * 
 */
package com.momega.spacesimulator.server.data;

/**
 * @author martin
 *
 */
public class MemoryCollectionFactory implements CollectionFactory {

	@Override
	public <T> Collection<T> get(String name, Class<T> clazz) {
		MemoryCollection<T> collection = new MemoryCollection<>();
		return collection;
	}

}
