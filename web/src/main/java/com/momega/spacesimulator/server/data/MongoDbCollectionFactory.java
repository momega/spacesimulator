/**
 * 
 */
package com.momega.spacesimulator.server.data;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * @author martin
 *
 */
public class MongoDbCollectionFactory implements CollectionFactory, DisposableBean {
	
	private static final Logger logger = LoggerFactory.getLogger(MongoDbCollectionFactory.class);

	private MongoDatabase database;
	private MongoClient mongoClient;
	private Map<Class<?>, ElementSerializer<?>> serializers = new HashMap<>();

	public MongoDbCollectionFactory() {
		super();
	}
	
	public void connect() {
		MongoClientURI uri= new MongoClientURI("mongodb://spacesimulator:spacesimulator@ds041651.mongolab.com:41651/spacesimulator");
		mongoClient = new MongoClient(uri);
		database = mongoClient.getDatabase(uri.getDatabase());
		logger.info("connection to mongo database");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> Collection<T> get(String name, Class<T> clazz) {
		Assert.notNull("mongo database not initialized");
		MongoCollection<Document> collection = database.getCollection(name);
		MongoDbCollection<T> result = new MongoDbCollection<>();
		result.setCollection(collection);
		ElementSerializer<?> serializer = serializers.get(clazz);
		if (serializer == null) {
			serializer = new SimpleElementSerializer(clazz);
		}
		result.setSerializer((ElementSerializer<T>) serializer);
		return result;
	}
	
	public void addSerializer(Class<?> clazz, ElementSerializer<?> serializer) {
		serializers.put(clazz, serializer);
	}

	@Override
	public void destroy() throws Exception {
		logger.info("closing mongo database");
		mongoClient.close();
	}

}
