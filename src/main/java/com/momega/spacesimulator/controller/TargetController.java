package com.momega.spacesimulator.controller;

import javax.media.opengl.awt.GLCanvas;

import com.momega.spacesimulator.renderer.ViewCoordinates;
import com.momega.spacesimulator.renderer.RendererModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GLDrawable;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Controller which enables selecting the target
 * Created by martin on 6/7/14.
 */
public class TargetController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(TargetController.class);

    @Override
    public void mouseClicked(MouseEvent e) {
        GLCanvas canvas  = (GLCanvas) e.getSource();
        GLDrawable drawable = canvas.getDelegatedDrawable();
        int x = e.getX();
        int y = e.getY();
        if (drawable.isGLOriented()) {
            y = drawable.getHeight() - y;
        }
        logger.info("click count = {}, button  = {}", e.getClickCount(), e.getButton());
        final ViewCoordinates  viewCoordinates = RendererModel.getInstance().findViewCoordinates(x, y);
        if (viewCoordinates != null && e.getButton() >= MouseEvent.BUTTON2) {
            final JPopupMenu popup = new JPopupMenu();

            JMenuItem detailItem = new JMenuItem("Detail");
            popup.add(detailItem);
            JMenuItem selectItem = new JMenuItem("Select");
            popup.add(selectItem);
            selectItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    select(viewCoordinates);
                }
            });

            popup.show(e.getComponent(), e.getX(), e.getY());
        } else if (viewCoordinates != null && e.getClickCount() > 1) {
            select(viewCoordinates);
        }
        super.mouseClicked(e);
    }

    protected void  select(ViewCoordinates viewCoordinates) {
        RendererModel.getInstance().selectDynamicalPoint(viewCoordinates);
    }

}
