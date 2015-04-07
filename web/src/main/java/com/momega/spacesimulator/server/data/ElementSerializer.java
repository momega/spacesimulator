/**
 * 
 */
package com.momega.spacesimulator.server.data;

import org.bson.Document;

/**
 * @author martin
 *
 */
public interface ElementSerializer<T> {

	T deserialize(Document document);

	Document serialize(T item);
	
}
