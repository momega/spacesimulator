package com.momega.spacesimulator.builder;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.service.ModelSerializer;

@Component
public class ByteArrayModelBuilder implements ModelBuilder {
	
	@Autowired
	private ModelSerializer modelSerializer;

	private byte[] data;

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Model build() {
		InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(data));
		Model model = modelSerializer.load(reader);
		return model;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
