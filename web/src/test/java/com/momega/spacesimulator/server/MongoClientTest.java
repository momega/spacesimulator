/**
 * 
 */
package com.momega.spacesimulator.server;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * @author martin
 *
 */
public class MongoClientTest {
	
	@Test
	public void herokuTest() throws UnknownHostException {
		MongoCredential credential = MongoCredential.createMongoCRCredential("heroku_app25459577", "heroku_app25459577", "gsps2dbe8l7ovbot1jkto5lnid".toCharArray());
		MongoClient mongoClient = new MongoClient(new ServerAddress("ds041841.mongolab.com" , 41841), 
								Arrays.asList(credential));
		DB db = mongoClient.getDB("heroku_app25459577");
		Set<String> list = db.getCollectionNames();
		System.out.println(list);		
	}
	
	@Test
	public void herokuUriTest() throws UnknownHostException {
		MongoClientURI uri= new MongoClientURI("mongodb://heroku_app25459577:gsps2dbe8l7ovbot1jkto5lnid@ds041841.mongolab.com:41841/heroku_app25459577");
		MongoClient mongoClient = new MongoClient(uri);
		DB db = mongoClient.getDB(uri.getDatabase());
		Set<String> list = db.getCollectionNames();
		System.out.println(list);
	}

	@Test
	public void simpleTest() throws UnknownHostException {
		MongoClientURI uri= new MongoClientURI("mongodb://spacesimulator:spacesimulator@ds041651.mongolab.com:41651/spacesimulator");
		MongoClient mongoClient = new MongoClient(uri);
		DB db = mongoClient.getDB(uri.getDatabase());
		Set<String> list = db.getCollectionNames();
		System.out.println(list);
		DBCollection projects = db.getCollection("projects");
		Assert.assertNotNull(projects);
	}

}
