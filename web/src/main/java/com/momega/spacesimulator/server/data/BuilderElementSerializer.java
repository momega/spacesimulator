/**
 * 
 */
package com.momega.spacesimulator.server.data;

import org.bson.Document;

import com.momega.spacesimulator.server.controller.Builder;

/**
 * @author martin
 *
 */
public class BuilderElementSerializer extends SimpleElementSerializer<Builder> {

	public BuilderElementSerializer() {
		super(Builder.class);
	}
	
	@Override
	public Builder deserialize(Document document) {
		Builder b = super.deserialize(document);
		String id = MongoDbCollection.getId(document);
		b.setId(id);
		return b;
	}

}
