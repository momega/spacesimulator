package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.context.Application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by martin on 12/30/14.
 */
public class ModelBuilderObjectModel extends AbstractObjectsModel<ModelBuilder> {

    public ModelBuilderObjectModel() {
        super(new ArrayList<>(Application.getInstance().getBeans(ModelBuilder.class).values()));
    }
}
