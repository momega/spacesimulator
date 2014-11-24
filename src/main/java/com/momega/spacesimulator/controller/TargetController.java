package com.momega.spacesimulator.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.IconProvider;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.renderer.ViewCoordinates;
import com.momega.spacesimulator.swing.DetailDialogHolder;
import com.momega.spacesimulator.swing.SwingUtils;
import com.momega.spacesimulator.swing.TimeDialog;

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
        Point position = GLUtils.getPosition(e);
        logger.info("click count = {}, button  = {}", e.getClickCount(), e.getButton());
        final Timestamp timestamp = ModelHolder.getModel().getTime();
        final List<ViewCoordinates> viewCoordinatesList = RendererModel.getInstance().findViewCoordinates(position);
        if (e.getButton() >= MouseEvent.BUTTON2) {
            final JPopupMenu popup = new JPopupMenu();
            for(final ViewCoordinates viewCoordinates : viewCoordinatesList) {
                PositionProvider positionProvider = viewCoordinates.getObject();
                String objectName = positionProvider.getName();
                JMenuItem detailItem = new JMenuItem("Detail of " + objectName + " ...");
                detailItem.setActionCommand(DETAIL_POPUP_POSITION_PROVIDER);
                detailItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showDialog(viewCoordinates.getObject());
					}
				});
                if (viewCoordinates.getObject() instanceof IconProvider) {
                	IconProvider iconProvider = (IconProvider) viewCoordinates.getObject();
                    detailItem.setIcon(SwingUtils.createImageIcon(iconProvider.getIcon()));
                }
                popup.add(detailItem);

                JMenuItem selectItem = new JMenuItem("Select of " + objectName);
                selectItem.setActionCommand(SELECT_POSITION_PROVIDER);
                popup.add(selectItem);
                selectItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        select(viewCoordinates);
                    }
                });

                if (positionProvider.getTimestamp().compareTo(timestamp)>0) {
                    JMenuItem timeItem = new JMenuItem("Time to " + objectName + " ...");
                    timeItem.setActionCommand(TIME_TO);
                    popup.add(timeItem);
                    timeItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            showTimeDialog(viewCoordinates.getObject());
                        }
                    });
                }

                popup.addSeparator();
            }
            JMenuItem onScreenItem = new JMenuItem("On Screen...");
            popup.add(onScreenItem);
            onScreenItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] possibilities = RendererModel.getInstance().findVisibleObjects();
                    String selectedName = (String) JOptionPane.showInputDialog(
                            null,
                            "Please select the visible object:",
                            "Visible objects",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            possibilities,
                            null);
                    ViewCoordinates vc = RendererModel.getInstance().findByName(selectedName);
                    if (vc != null) {
                        showDialog(vc.getObject());
                    }
                }
            });
            popup.show(e.getComponent(), e.getX(), e.getY());
        } else if (e.getClickCount() > 1) {
            logger.info("click count = {}", e.getClickCount());
            logger.info("list = {}", viewCoordinatesList.size());
            if (viewCoordinatesList.size()==1) {
                select(viewCoordinatesList.get(0));
            } else if (viewCoordinatesList.size()>1) {
                final JPopupMenu popup = new JPopupMenu();
                for(final ViewCoordinates viewCoordinates : viewCoordinatesList) {
                    String objectName = viewCoordinates.getObject().getName();
                    JMenuItem detailItem = new JMenuItem("Select of " + objectName + " ...");
                    detailItem.setActionCommand(DETAIL_POPUP_POSITION_PROVIDER);
                    detailItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            select(viewCoordinates);
                        }
                    });
                    popup.add(detailItem);
                }
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
        super.mouseClicked(e);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	if (SELECT_POSITION_PROVIDER.equals(e.getActionCommand())) {
    		PositionProvider positionProvider = (PositionProvider) RendererModel.getInstance().getMovingObjectsModel().getSelectedItem();
    		if (positionProvider != null) {
	    		ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(positionProvider);
	    		if (viewCoordinates != null) {
	    			select(viewCoordinates);
	    		}
    		}
    	} else if (DETAIL_POSITION_PROVIDER.equals(e.getActionCommand())) {
    		PositionProvider positionProvider = (PositionProvider) RendererModel.getInstance().getMovingObjectsModel().getSelectedItem();
    		showDialog(positionProvider);
    	}
    }

    protected void showTimeDialog(PositionProvider positionProvider) {
        // TODO: How to forward events
        final TimeDialog timeDialog = new TimeDialog(window, positionProvider.getTimestamp());
        SwingUtils.openDialog(timeDialog);
    }
    
    protected void showDialog(PositionProvider positionProvider) {
    	 DetailDialogHolder.getInstance().showDialog(positionProvider);
    }

    protected void select(ViewCoordinates viewCoordinates) {
        RendererModel.getInstance().selectItem(viewCoordinates);
    }

}
