package com.momega.spacesimulator.server;

import org.junit.Assert;
import org.junit.Test;

import com.momega.spacesimulator.server.data.Collection;
import com.momega.spacesimulator.server.data.MongoDbCollectionFactory;

public class MongoCollectionTest {

	@Test
	public void saveAndLoadTest() throws Exception {
		MongoDbCollectionFactory factory = new MongoDbCollectionFactory();
		factory.connect();
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
		factory.destroy();
	}
}
