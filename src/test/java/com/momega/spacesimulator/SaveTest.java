/**
 * 
 */
package com.momega.spacesimulator;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;

import com.momega.spacesimulator.context.Application;
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
		Application application = Application.getInstance();
        application.init(0);
        for(int i=0; i<1000; i++)
        	application.next(false,  BigDecimal.ONE);
        
        ModelSerializer serializer = Application.getInstance().getService(ModelSerializer.class);
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
