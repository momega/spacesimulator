package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.renderer.RendererModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Created by martin on 12/30/14.
 */
public class ModelBuilderPanel extends AbstractDefaultPanel {

    private static final Logger logger = LoggerFactory.getLogger(DeleteSpacecraftPanel.class);

    private static final long serialVersionUID = -2671510388181726431L;
    private ModelBuilderObjectModel modelBuilderObjectModel;

    public ModelBuilderPanel() {
        modelBuilderObjectModel = new ModelBuilderObjectModel();

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel lblBuilder = new JLabel("Model Builder:", SwingConstants.RIGHT);
        JComboBox<ModelBuilder> builderBox = new JComboBox<ModelBuilder>(modelBuilderObjectModel);
        builderBox.setRenderer(new ModelBuilderListRenderer());

        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblBuilder)
                        .addComponent(builderBox)));

        layout.setHorizontalGroup(layout
                .createSequentialGroup()
                .addGroup(
                        layout.createParallelGroup(
                                GroupLayout.Alignment.LEADING).addComponent(
                                lblBuilder))
                .addGroup(
                        layout.createParallelGroup(
                                GroupLayout.Alignment.LEADING).addComponent(
                                builderBox)));

    }

    @Override
    public boolean okPressed() {
        ModelBuilder modelBuilder = (ModelBuilder) modelBuilderObjectModel.getSelectedItem();
        if (modelBuilder == null) {
            return false;
        }
        logger.info("model builder selected = {}", modelBuilder.getName());
        RendererModel.getInstance().setModelBuilderRequested(modelBuilder);
        return true;
    }
}
