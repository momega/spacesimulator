package com.momega.spacesimulator.controller;

import javax.media.opengl.awt.GLCanvas;

import com.momega.spacesimulator.renderer.ViewCoordinates;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.swing.DetailDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GLDrawable;
import javax.swing.*;
import java.awt.*;
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
        final GLCanvas canvas  = (GLCanvas) e.getSource();
        GLDrawable drawable = canvas.getDelegatedDrawable();
        final Point position = getPosition(drawable, e);
        logger.info("click count = {}, button  = {}", e.getClickCount(), e.getButton());
        final ViewCoordinates  viewCoordinates = RendererModel.getInstance().findViewCoordinates(position);
        if (viewCoordinates != null && e.getButton() >= MouseEvent.BUTTON2) {
            final JPopupMenu popup = new JPopupMenu();

            JMenuItem detailItem = new JMenuItem("Detail");
            popup.add(detailItem);
            detailItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DetailDialog dialog = new DetailDialog((java.awt.Frame) canvas.getParent(), viewCoordinates.getObject());
                    dialog.setLocation(position);
                    dialog.setVisible(true);

                }
            });
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

    protected Point getPosition(GLDrawable drawable, MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (drawable.isGLOriented()) {
            y = drawable.getHeight() - y;
        }
        return new Point(x, y);
    }

    protected void  select(ViewCoordinates viewCoordinates) {
        RendererModel.getInstance().selectDynamicalPoint(viewCoordinates);
    }

}
