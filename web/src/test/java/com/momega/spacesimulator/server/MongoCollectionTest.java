package com.momega.spacesimulator.server;

import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Test;

import com.momega.spacesimulator.server.data.Collection;
import com.momega.spacesimulator.server.data.MongoCollectionFactory;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoCollectionTest {

	@Test
	public void saveAndLoadTest() throws UnknownHostException {
		MongoClientURI uri= new MongoClientURI("mongodb://spacesimulator:spacesimulator@ds041651.mongolab.com:41651/spacesimulator");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase db = mongoClient.getDatabase(uri.getDatabase());
		
		MongoCollectionFactory factory = new MongoCollectionFactory(db);
		Collection<TestObject> coll = factory.get("test", TestObject.class);
		
		TestObject testObject = new TestObject();
		testObject.setFirstName("Pokus1"); testObject.setLastName("PokusX");
		String key = coll.add(testObject);
		Assert.assertNotNull(key);
		
		TestObject testGetObject = coll.get(key);
		Assert.assertNotNull(testGetObject);
		Assert.assertEquals(testObject.getFirstName(), testGetObject.getFirstName());
		Assert.assertEquals(testObject.getLastName(), testGetObject.getLastName());
		
		Assert.assertEquals(1, coll.getAll().size());
		
		testGetObject.setLastName("Smudla");
		coll.update(key, testGetObject);
		
		TestObject testUpdatedObject = coll.get(key);
		Assert.assertEquals(testGetObject.getLastName(), testUpdatedObject.getLastName());
		
		coll.remove(key);
		
		Assert.assertEquals(0, coll.getAll().size());
		mongoClient.close();
	}
}
