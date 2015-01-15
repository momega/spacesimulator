/**
 * 
 */
package com.momega.spacesimulator;

import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.Assert;

import org.junit.Before;
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
public class SaveTest extends AbstractMissionTest {
	
	 private static final int CHECK_TIME = 4*60*60;
	
	@Before
    public void setup() {
        setup(SimpleSolarSystemModelBuilder.class);
    }

	@Test
	public void simpleTest() {
		DefaultApplication application = new DefaultApplication(AppConfig.class);
		runTo(CHECK_TIME);
        
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
