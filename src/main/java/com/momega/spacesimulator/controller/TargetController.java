package com.momega.spacesimulator.controller;

import javax.media.opengl.awt.GLCanvas;

import com.momega.spacesimulator.renderer.ViewCoordinates;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.swing.DetailDialogHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GLDrawable;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Controller which enables functionality after clicking to the screen
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
        if (e.getButton() >= MouseEvent.BUTTON2) {
            final JPopupMenu popup = new JPopupMenu();

            if (viewCoordinates!=null) {
                JMenuItem detailItem = new JMenuItem("Detail...");
                popup.add(detailItem);
                detailItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DetailDialogHolder.getInstance().showDialog(position, viewCoordinates.getObject());
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

                popup.addSeparator();
            }

            JMenuItem onScreenItem = new JMenuItem("On Screen...");
            popup.add(onScreenItem);
            onScreenItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] possibilities = RendererModel.getInstance().findVisibleObjects();
                    String selectedName = (String)JOptionPane.showInputDialog(
                            null,
                            "Please select the visible object:",
                            "On the screen",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            possibilities,
                            null);
                    ViewCoordinates vc = RendererModel.getInstance().findByName(selectedName);
                    if (vc !=null) {
                    	DetailDialogHolder.getInstance().showDialog(position, vc.getObject());
                    }
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
