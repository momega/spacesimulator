package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.builder.ModelBuilder;

import javax.swing.*;

/**
 * List renderer for the model builders
 * Created by martin on 12/30/14.
 */
public class ModelBuilderListRenderer extends AbstractObjectListRenderer<ModelBuilder> {

	private static final long serialVersionUID = 5542299320496252401L;

	@Override
    protected String getText(ModelBuilder value) {
        return value.getName();
    }

    @Override
    protected ImageIcon getIcon(ModelBuilder value) {
        return null;
    }
}
