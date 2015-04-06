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
public abstract class AbstractMongoCollection<T> implements Collection<T> {

	private MongoCollection<Document> collection;
	protected Class<T> clazz;
	
	
	protected AbstractMongoCollection() {
	}
	
	protected Bson id(String id) {
		return eq("_id", new ObjectId(id));
	}

	@Override
	public String add(T item) {
		Document document = serialize(item);
		collection.insertOne(document);
		ObjectId id = (ObjectId)document.get( "_id" );
		return id.toString();
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
	public Map<String, T> getAll() {
		MongoCursor<Document> cursor = collection.find().iterator();
		Map<String, T> result = new HashMap<>();
		try {
		   while(cursor.hasNext()) {
		       Document dbo = cursor.next();
		       T obj = deserialize(dbo);
		       ObjectId key = (ObjectId) dbo.get("_id");
		       result.put(key.toString(), obj);
		   }
		} finally {
		   cursor.close();
		}
		return result;
	}

	protected abstract T deserialize(Document dbo);
	
	protected abstract Document serialize(T item);

	public void setCollection(MongoCollection<Document> collection) {
		this.collection = collection;
	}
	
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

}
