/**
 * 
 */
package com.momega.spacesimulator.server.data;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * @author martin
 *
 */
public class MongoCollectionFactory implements CollectionFactory {

	private MongoDatabase db;

	public MongoCollectionFactory(MongoDatabase db) {
		this.db = db;
	}

	@Override
	public <T> Collection<T> get(String name, Class<T> clazz) {
		MongoCollection<Document> collection = db.getCollection(name);
		SimpleMongoCollection<T> result = new SimpleMongoCollection<>();
		result.setCollection(collection);
		result.setClazz(clazz);
		return result;
	}

}
