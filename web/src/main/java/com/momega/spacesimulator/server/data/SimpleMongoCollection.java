/**
 * 
 */
package com.momega.spacesimulator.server.data;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * @author martin
 *
 */
public class SimpleMongoCollection<T> extends AbstractMongoCollection<T> {
	
	private Gson gson = new Gson();
	
	@Override
	protected T deserialize(DBObject dbo) {
		String json = JSON.serialize(dbo);
		T result = gson.fromJson(json, clazz);
		return result;
	}

	@Override
	protected DBObject serialize(T item) {
		String json = gson.toJson(item); 
		DBObject bson = ( DBObject ) JSON.parse( json );
		return bson;
	}

}
