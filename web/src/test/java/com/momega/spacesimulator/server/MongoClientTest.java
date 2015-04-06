/**
 * 
 */
package com.momega.spacesimulator.server;

import java.net.UnknownHostException;
import java.util.Arrays;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

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
		MongoDatabase db = mongoClient.getDatabase("heroku_app25459577");
		ListCollectionsIterable<Document> list = db.listCollections();
		System.out.println(list.toString());		
		mongoClient.close();
	}
	
	@Test
	public void herokuUriTest() throws UnknownHostException {
		MongoClientURI uri= new MongoClientURI("mongodb://heroku_app25459577:gsps2dbe8l7ovbot1jkto5lnid@ds041841.mongolab.com:41841/heroku_app25459577");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase db = mongoClient.getDatabase(uri.getDatabase());
		ListCollectionsIterable<Document> list = db.listCollections();
		System.out.println(list);
		mongoClient.close();
	}

	@Test
	public void simpleTest() throws UnknownHostException {
		MongoClientURI uri= new MongoClientURI("mongodb://spacesimulator:spacesimulator@ds041651.mongolab.com:41651/spacesimulator");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase db = mongoClient.getDatabase(uri.getDatabase());
		ListCollectionsIterable<Document> list = db.listCollections();
		System.out.println(list);
		MongoCollection<Document> projects = db.getCollection("projects");
		Assert.assertNotNull(projects);
		mongoClient.close();
	}

}
