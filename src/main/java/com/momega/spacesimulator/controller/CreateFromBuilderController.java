package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.builder.AbstractModelBuilder;
import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.swing.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Map;

/**
 * Created by martin on 12/30/14.
 */
public class CreateFromBuilderController extends AbstractController {

    public static final String CREATE_FROM_BUILDER_COMMAND = "create_from_builder";

    @Override
    public void actionPerformed(ActionEvent e) {
        if (CREATE_FROM_BUILDER_COMMAND.equals(e.getActionCommand())) {
            Map<String, ModelBuilder> beans = Application.getInstance().getBeans(ModelBuilder.class);
            String[] beanNames = beans.keySet().toArray(new String[beans.size()]);
            String value = (String) JOptionPane.showInputDialog(null, "Select the Builder", "Create Model from Builder", JOptionPane.PLAIN_MESSAGE,
                    Icons.CREATE_FROM_BUILDER, beanNames, null);
            ModelBuilder modelBuilder = beans.get(value);
            RendererModel.getInstance().setModelBuilderRequested(modelBuilder);
        }
    }
}
