/**
 * 
 */
package com.momega.spacesimulator.server;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * @author martin
 *
 */
public class TestClient {

	@Test
	public void simpleTest() throws UnknownHostException {
		MongoCredential credential = MongoCredential.createMongoCRCredential("heroku_app25459577", "heroku_app25459577", "gsps2dbe8l7ovbot1jkto5lnid".toCharArray());
		MongoClient mongoClient = new MongoClient(new ServerAddress("ds041841.mongolab.com" , 41841), 
								Arrays.asList(credential));
		DB db = mongoClient.getDB("heroku_app25459577");
		Set<String> list = db.getCollectionNames();
		System.out.println(list);
	}

}
