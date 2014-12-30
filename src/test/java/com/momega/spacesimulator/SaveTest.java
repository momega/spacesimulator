/**
 * 
 */
package com.momega.spacesimulator;

import com.momega.spacesimulator.builder.MediumSolarSystemModelBuilder;
import com.momega.spacesimulator.context.DefaultApplication;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.service.ModelSerializer;
import junit.framework.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

/**
 * @author martin
 *
 */
public class SaveTest {

	@Test
	public void simpleTest() {
		DefaultApplication application = new DefaultApplication(TestAppConfig.class);
        application.init(MediumSolarSystemModelBuilder.class);
        for(int i=0; i<1000; i++)
        	application.next(false,  BigDecimal.ONE);
        
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
