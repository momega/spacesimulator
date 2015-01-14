/**
 * 
 */
package com.momega.spacesimulator;

import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.Assert;

import org.junit.Test;

import com.momega.spacesimulator.builder.SimpleSolarSystemModelBuilder;
import com.momega.spacesimulator.context.AppConfig;
import com.momega.spacesimulator.context.DefaultApplication;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.service.ModelSerializer;

/**
 * @author martin
 *
 */
public class SaveTest {

	@Test
	public void simpleTest() {
		DefaultApplication application = new DefaultApplication(AppConfig.class);
        application.init(SimpleSolarSystemModelBuilder.class);
        for(int i=0; i<4*60*60; i++) {
        	application.next(true,  1.0);
        }
        application.next(false,  1.0);
        
        ModelSerializer serializer = application.getService(ModelSerializer.class);
        StringWriter writer = new StringWriter();
        serializer.save(ModelHolder.getModel(), writer);
        String s = writer.getBuffer().toString();
        StringReader reader = new StringReader(s);
        Model m = serializer.load(reader);
        writer = new StringWriter();
        serializer.save(m, writer);
        String s2 = writer.getBuffer().toString();
        Assert.assertEquals(s, s2);
	}

}
