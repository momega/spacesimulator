/**
 * 
 */
package com.momega.spacesimulator.server.data;

import org.bson.Document;

import com.google.gson.Gson;

/**
 * @author martin
 *
 */
public class SimpleElementSerializer<T> implements ElementSerializer<T> {
	
	private Class<T> clazz;

	public SimpleElementSerializer(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	private Gson gson = new Gson();
	
	@Override
	public T deserialize(Document document) {
		String json = document.toJson();
		T result = gson.fromJson(json, clazz);
		return result;
	}

	@Override
	public Document serialize(T item) {
		String json = gson.toJson(item); 
		Document document = Document.parse( json );
		return document;
	}

}
