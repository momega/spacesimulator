/**
 * 
 */
package com.momega.spacesimulator.server.data;

/**
 * @author martin
 *
 */
public interface CollectionFactory {

	<T> Collection<T> get(String name, Class<T> clazz);
}
