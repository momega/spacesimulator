/**
 * 
 */
package com.momega.spacesimulator.server.data;

import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * @author martin
 *
 */
public class MongoCollectionFactory implements CollectionFactory {

	private DB db;

	public MongoCollectionFactory(DB db) {
		this.db = db;
	}

	@Override
	public <T> Collection<T> get(String name, Class<T> clazz) {
		DBCollection dbCollection = db.getCollection(name);
		SimpleMongoCollection<T> result = new SimpleMongoCollection<>();
		result.setDbCollection(dbCollection);
		result.setClazz(clazz);
		return result;
	}

}
