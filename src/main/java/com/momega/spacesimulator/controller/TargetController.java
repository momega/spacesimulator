package com.momega.spacesimulator.controller;

import javax.media.opengl.awt.GLCanvas;

import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.renderer.ViewCoordinates;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.swing.DetailDialogHolder;
import com.momega.spacesimulator.swing.WindowModel;

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
    
    public static final String SELECT_POSITION_PROVIDER = "select_position_provider";
    
    public static final String DELAIL_POSITION_PROVIDER = "detail_position_provider";
    
    public static final String DELAIL_POPUP_POSITION_PROVIDER = "detail_position_provider";

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
                detailItem.setActionCommand(DELAIL_POPUP_POSITION_PROVIDER);
                detailItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showDialog(viewCoordinates.getObject());
					}
				});
                popup.add(detailItem);

                JMenuItem selectItem = new JMenuItem("Select");
                selectItem.setActionCommand(DELAIL_POSITION_PROVIDER);
                popup.add(selectItem);
                selectItem.addActionListener(this);

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
                            "Visible objects",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            possibilities,
                            null);
                    ViewCoordinates vc = RendererModel.getInstance().findByName(selectedName);
                    if (vc !=null) {
                    	showDialog(vc.getObject());
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
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	if (SELECT_POSITION_PROVIDER.equals(e.getActionCommand())) {
    		PositionProvider positionProvider = (PositionProvider) WindowModel.getInstance().getMovingObjectsModel().getSelectedItem();
    		if (positionProvider != null) {
	    		ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(positionProvider);
	    		select(viewCoordinates);
    		}
    	} else if (DELAIL_POSITION_PROVIDER.equals(e.getActionCommand())) {
    		PositionProvider positionProvider = (PositionProvider) WindowModel.getInstance().getMovingObjectsModel().getSelectedItem();
    		showDialog(positionProvider);
    	}
    }
    
    protected void showDialog(PositionProvider positionProvider) {
    	 DetailDialogHolder.getInstance().showDialog(positionProvider);
    }

    protected void select(ViewCoordinates viewCoordinates) {
        RendererModel.getInstance().selectViewCoordinates(viewCoordinates);
        WindowModel.getInstance().setSelectedItem(viewCoordinates.getObject());
    }

}
