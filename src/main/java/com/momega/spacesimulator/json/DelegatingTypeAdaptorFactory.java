package com.momega.spacesimulator.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DelegatingTypeAdaptorFactory implements TypeAdapterFactory {
	
	private Map<Class<?>, Serializer<?>> serializers = new HashMap<>();

	public DelegatingTypeAdaptorFactory() {
		super();
	}

	@Override
	public <T> TypeAdapter<T> create(final Gson gson, TypeToken<T> type) {
		Class<?> rawType = type.getRawType();
		final List<Serializer<T>> list = new ArrayList<>();
		for(Map.Entry<Class<?>, Serializer<?>> entry : serializers.entrySet()) {
			Class<?> clazz = entry.getKey();
			if (rawType.isAssignableFrom(clazz)) {
				list.add((Serializer<T>) entry.getValue());
			}
			if (clazz.isAssignableFrom(rawType)) {
				list.add((Serializer<T>) entry.getValue());
			}
		}
		
		if (list.isEmpty()) {
			return null;
		}
		
		final TypeAdapter<T> delegate = (TypeAdapter<T>) gson.getDelegateAdapter(this, type);
		final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
		final DelegatingTypeAdaptorFactory thisFactory = this;
		
		TypeAdapter<T> result = (TypeAdapter<T>) new TypeAdapter<T>() {
			
			@Override
			public void write(JsonWriter writer, T value) throws IOException {
				if (value == null) {
					writer.nullValue();
					return;
				}
				JsonObject object = (JsonObject) delegate.toJsonTree(value);
				for(Serializer<T> serializer : list) {
					serializer.write(object, value);
				}
				elementAdapter.write(writer, object);
			}

			@Override
			public T read(JsonReader reader) throws IOException {
			    JsonElement jsonElement = (JsonElement) elementAdapter.read(reader);

			    if (!jsonElement.isJsonObject()) {
			        return delegate.fromJsonTree(jsonElement);
			    }

			    JsonObject jsonObject = jsonElement.getAsJsonObject();
			    Class<?> newClass = null;
			    for(Serializer<T> serializer : list) {
			    	newClass = serializer.getClass(jsonObject);
			    	if (newClass != null) {
			    		break;
			    	}
			    }
			    
			    T value;
			    if (newClass != null) {
			    	value = (T) gson.getDelegateAdapter(thisFactory, TypeToken.get(newClass)).fromJsonTree(jsonElement);
			    } else {
			    	value = delegate.fromJsonTree(jsonElement); 
			    }
			    
			    for(Serializer<T> serializer : list) {
			    	if (serializer.getSuperClass().isAssignableFrom(value.getClass())) {
			    		serializer.read(jsonObject, value);
			    	}
			    }
				return value;
			}
			
		};
		return result;
	}
	
	public void registerSerializer(Class<?> clazz, Serializer<?> serializer) {
		serializers.put(clazz, serializer);
	}

}
