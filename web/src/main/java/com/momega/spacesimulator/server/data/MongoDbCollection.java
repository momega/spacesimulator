/**
 * 
 */
package com.momega.spacesimulator.server.data;

import static com.mongodb.client.model.Filters.eq;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

/**
 * @author martin
 *
 */
public class MongoDbCollection<T> implements Collection<T> {

	private MongoCollection<Document> collection;
	private ElementSerializer<T> serializer;
	
	private static final String ID = "_id";
	
	protected Bson id(String id) {
		return eq(ID, new ObjectId(id));
	}
	
	public static String getId(Document document) {
		ObjectId id = (ObjectId)document.get(ID);
		return id.toString();
	}

	@Override
	public String add(T item) {
		Document document = serialize(item);
		collection.insertOne(document);
		return getId(document);
	}

	@Override
	public void update(String id, T item) {
		Document dbo = serialize(item);
		collection.replaceOne(id(id), dbo);
	}

	@Override
	public T get(String id) {
		Document dbo = collection.find(id(id)).first();
		T result = deserialize(dbo);
		return result;
	}

	@Override
	public void remove(String id) {
		collection.deleteOne(id(id));
	}
	
	@Override
	public long size() {
		return collection.count();
	}

	@Override
	public Map<String, T> getAll() {
		MongoCursor<Document> cursor = collection.find().iterator();
		Map<String, T> result = new HashMap<>();
		try {
		   while(cursor.hasNext()) {
		       Document document = cursor.next();
		       T obj = deserialize(document);
		       result.put(getId(document), obj);
		   }
		} finally {
		   cursor.close();
		}
		return result;
	}

	protected T deserialize(Document document) {
		return serializer.deserialize(document);
	}
	
	protected Document serialize(T item) {
		return serializer.serialize(item);
	}

	public void setCollection(MongoCollection<Document> collection) {
		this.collection = collection;
	}
	
	public void setSerializer(ElementSerializer<T> serializer) {
		this.serializer = serializer;
	}

}
