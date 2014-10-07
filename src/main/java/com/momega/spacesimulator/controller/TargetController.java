package com.momega.spacesimulator.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.media.opengl.GLDrawable;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.*;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.swing.TimeDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.renderer.ViewCoordinates;
import com.momega.spacesimulator.swing.DetailDialogHolder;

/**
 * Controller which enables functionality after clicking to the screen
 * Created by martin on 6/7/14.
 */
public class TargetController extends AbstractController {

    private final DefaultWindow window;

    private static final Logger logger = LoggerFactory.getLogger(TargetController.class);

    public static final String SELECT_POSITION_PROVIDER = "select_position_provider";

    public static final String DETAIL_POSITION_PROVIDER = "detail_position_provider";

    public static final String DETAIL_POPUP_POSITION_PROVIDER = "detail_position_provider";

    public static final String TIME_TO = "time_to";

    public TargetController(DefaultWindow window) {
        this.window = window;
    }

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
                detailItem.setActionCommand(DETAIL_POPUP_POSITION_PROVIDER);
                detailItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showDialog(viewCoordinates.getObject());
					}
				});
                popup.add(detailItem);

                JMenuItem selectItem = new JMenuItem("Select");
                selectItem.setActionCommand(DETAIL_POSITION_PROVIDER);
                popup.add(selectItem);
                selectItem.addActionListener(this);

                JMenuItem timeItem = new JMenuItem("Time to...");
                timeItem.setActionCommand(TIME_TO);
                popup.add(timeItem);
                timeItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showTimeDialog(viewCoordinates.getObject());
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
    		PositionProvider positionProvider = (PositionProvider) RendererModel.getInstance().getMovingObjectsModel().getSelectedItem();
    		if (positionProvider != null) {
	    		ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(positionProvider);
	    		select(viewCoordinates);
    		}
    	} else if (DETAIL_POSITION_PROVIDER.equals(e.getActionCommand())) {
    		PositionProvider positionProvider = (PositionProvider) RendererModel.getInstance().getMovingObjectsModel().getSelectedItem();
    		showDialog(positionProvider);
    	}
    }

    protected void showTimeDialog(PositionProvider positionProvider) {
        // TODO: How to forward events
        final TimeDialog timeDialog = new TimeDialog(window, positionProvider.getTimestamp());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                timeDialog.setVisible(true);
            }
        });
    }
    
    protected void showDialog(PositionProvider positionProvider) {
    	 DetailDialogHolder.getInstance().showDialog(positionProvider);
    }

    protected void select(ViewCoordinates viewCoordinates) {
        RendererModel.getInstance().selectItem(viewCoordinates);
    }

}
