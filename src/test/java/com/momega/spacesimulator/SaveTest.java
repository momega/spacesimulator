/**
 * 
 */
package com.momega.spacesimulator;

import java.lang.reflect.Modifier;

import junit.framework.Assert;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.json.CameraSerializer;
import com.momega.spacesimulator.json.DelegatingTypeAdaptorFactory;
import com.momega.spacesimulator.json.KeplerianOrbitSerializer;
import com.momega.spacesimulator.json.NamedObjectCache;
import com.momega.spacesimulator.json.NamedObjectSerializer;
import com.momega.spacesimulator.json.OrbitIntersectionSerializer;
import com.momega.spacesimulator.json.SphereOfInfluenceSerializer;
import com.momega.spacesimulator.json.TargetSerializer;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.NamedObject;
import com.momega.spacesimulator.model.OrbitIntersection;
import com.momega.spacesimulator.model.SphereOfInfluence;
import com.momega.spacesimulator.model.Target;

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
        	application.next(false);
        
        DelegatingTypeAdaptorFactory delegatingTypeAdaptorFactory = new DelegatingTypeAdaptorFactory();
        delegatingTypeAdaptorFactory.registerSerializer(OrbitIntersection.class, new OrbitIntersectionSerializer());
        delegatingTypeAdaptorFactory.registerSerializer(KeplerianOrbit.class, new KeplerianOrbitSerializer());
        delegatingTypeAdaptorFactory.registerSerializer(SphereOfInfluence.class, new SphereOfInfluenceSerializer());
        delegatingTypeAdaptorFactory.registerSerializer(Target.class, new TargetSerializer());
        delegatingTypeAdaptorFactory.registerSerializer(Camera.class, new CameraSerializer());
        delegatingTypeAdaptorFactory.registerSerializer(NamedObject.class, new NamedObjectSerializer());
        
        Gson gson = new GsonBuilder()
	        .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
	        .registerTypeAdapterFactory(delegatingTypeAdaptorFactory)
	        .create();
        
        NamedObjectCache.getInstance().clear();
        String s = gson.toJson(ModelHolder.getModel());
        
        Model m = gson.fromJson(s, Model.class);
        String s2 = gson.toJson(m);
        Assert.assertEquals(s, s2);
	}

}
