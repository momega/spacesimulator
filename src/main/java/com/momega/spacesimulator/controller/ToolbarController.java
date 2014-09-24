/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JToggleButton;

import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.swing.MovingObjectsModel;
import com.momega.spacesimulator.swing.WindowModel;

/**
 * @author martin
 *
 */
public class ToolbarController extends AbstractController {

	public static final String SPACECRAFT_TOGGLE_COMMAND = "toggle_spacecraft";
	public static final String CELESTIAL_TOGGLE_COMMAND = "toggle_celestial";
	public static final String POINT_TOGGLE_COMMAND = "toggle_point";

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case SPACECRAFT_TOGGLE_COMMAND:
			{
				JToggleButton button = (JToggleButton) e.getSource();
				boolean visible = button.getModel().isSelected();
				WindowModel.getInstance().setSpacecraftVisible(visible);
				updateSelectableMovingObjects();
			}
			break;
		case CELESTIAL_TOGGLE_COMMAND:
			{
				JToggleButton button = (JToggleButton) e.getSource();
				boolean visible = button.getModel().isSelected();
				WindowModel.getInstance().setCelestialVisible(visible);
				updateSelectableMovingObjects();
			}
			break;
		case POINT_TOGGLE_COMMAND:
			{
				JToggleButton button = (JToggleButton) e.getSource();
				boolean visible = button.getModel().isSelected();
				WindowModel.getInstance().setPointsVisible(visible);
				updateSelectableMovingObjects();
			}
			break;
		default:
			break;
		}
	}

	protected void updateSelectableMovingObjects() {
		List<PositionProvider> newItems = WindowModel.getInstance().selectMovingObjects();
		MovingObjectsModel movingObjectsModel = WindowModel.getInstance().getMovingObjectsModel();
    	movingObjectsModel.removeAllElements();
    	movingObjectsModel.addElements(newItems);	
	}
}
