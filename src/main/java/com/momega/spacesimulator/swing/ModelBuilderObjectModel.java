package com.momega.spacesimulator.swing;

import java.util.ArrayList;

import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.context.Application;

/**
 * Created by martin on 12/30/14.
 */
public class ModelBuilderObjectModel extends AbstractObjectsModel<ModelBuilder> {

	private static final long serialVersionUID = -5206734107957958585L;

	public ModelBuilderObjectModel() {
        super(new ArrayList<>(Application.getInstance().getBeans(ModelBuilder.class).values()));
    }
}
