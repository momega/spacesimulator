/**
 * 
 */
package com.momega.spacesimulator.server.data;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author martin
 *
 */
public abstract class AbstractMongoCollection<T> implements Collection<T> {

	private DBCollection dbCollection;
	protected Class<T> clazz;
	
	
	protected AbstractMongoCollection() {
	}

	@Override
	public String add(T item) {
		DBObject dbo = serialize(item);
		dbCollection.insert(dbo);
		ObjectId id = (ObjectId)dbo.get( "_id" );
		return id.toString();
	}

	@Override
	public void update(String id, T item) {
		DBObject dbo = serialize(item);
		BasicDBObject key = new BasicDBObject("_id", new ObjectId(id));
		dbCollection.update(key, dbo);
	}

	@Override
	public T get(String id) {
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
		DBObject dbo = dbCollection.findOne(query);
		T result = deserialize(dbo);
		return result;
	}

	@Override
	public void remove(String id) {
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
		dbCollection.remove(query);
	}

	@Override
	public Map<String, T> getAll() {
		DBCursor cursor = dbCollection.find();
		Map<String, T> result = new HashMap<>();
		try {
		   while(cursor.hasNext()) {
		       DBObject dbo = cursor.next();
		       T obj = deserialize(dbo);
		       ObjectId key = (ObjectId) dbo.get("_id");
		       result.put(key.toString(), obj);
		   }
		} finally {
		   cursor.close();
		}
		return result;
	}

	protected abstract T deserialize(DBObject dbo);
	
	protected abstract DBObject serialize(T item);
	
	public void setDbCollection(DBCollection dbCollection) {
		this.dbCollection = dbCollection;
	}
	
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

}
