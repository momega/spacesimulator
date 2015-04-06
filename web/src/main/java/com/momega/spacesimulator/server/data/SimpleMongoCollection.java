/**
 * 
 */
package com.momega.spacesimulator.server.data;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.util.JSON;

/**
 * @author martin
 *
 */
public class SimpleMongoCollection<T> extends AbstractMongoCollection<T> {
	
	private Gson gson = new Gson();
	
	@Override
	protected T deserialize(Document dbo) {
		String json = dbo.toJson();
		T result = gson.fromJson(json, clazz);
		return result;
	}

	@Override
	protected Document serialize(T item) {
		String json = gson.toJson(item); 
		Document bson = Document.parse( json );
		return bson;
	}

}
