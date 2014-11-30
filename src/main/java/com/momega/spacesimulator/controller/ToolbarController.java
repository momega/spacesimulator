/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JToggleButton;

import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.swing.PositionProvidersModel;

/**
 * @author martin
 *
 */
public class ToolbarController extends AbstractController {

	public static final String SPACECRAFT_TOGGLE_COMMAND = "toggle_spacecraft";
	public static final String CELESTIAL_TOGGLE_COMMAND = "toggle_celestial";
	public static final String POINT_TOGGLE_COMMAND = "toggle_point";
    public static final String HISTORY_POINT_TOGGLE_COMMAND = "toggle_history_point";

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case SPACECRAFT_TOGGLE_COMMAND:
			{
				JToggleButton button = (JToggleButton) e.getSource();
				boolean visible = button.getModel().isSelected();
				RendererModel.getInstance().setSpacecraftVisible(visible);
				updateSelectableMovingObjects();
			}
			break;
		case CELESTIAL_TOGGLE_COMMAND:
			{
				JToggleButton button = (JToggleButton) e.getSource();
				boolean visible = button.getModel().isSelected();
				RendererModel.getInstance().setCelestialVisible(visible);
				updateSelectableMovingObjects();
			}
			break;
		case POINT_TOGGLE_COMMAND:
			{
				JToggleButton button = (JToggleButton) e.getSource();
				boolean visible = button.getModel().isSelected();
				RendererModel.getInstance().setPointsVisible(visible);
				updateSelectableMovingObjects();
			}
			break;
        case HISTORY_POINT_TOGGLE_COMMAND:
            {
                JToggleButton button = (JToggleButton) e.getSource();
                boolean visible = button.getModel().isSelected();
                RendererModel.getInstance().setHistoryPointsVisible(visible);
                updateSelectableMovingObjects();
            }
            break;
		default:
			break;
		}
	}

	protected void updateSelectableMovingObjects() {
		List<PositionProvider> newItems = RendererModel.getInstance().selectPositionProviders();
		PositionProvidersModel movingObjectsModel = RendererModel.getInstance().getMovingObjectsModel();
    	movingObjectsModel.removeAllElements();
    	movingObjectsModel.addElements(newItems);	
	}
}
