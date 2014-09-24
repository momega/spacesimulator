/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ButtonModel;
import javax.swing.JToggleButton;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.AbstractOrbitalPoint;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.TrajectoryType;
import com.momega.spacesimulator.renderer.RendererModel;

/**
 * @author martin
 *
 */
public class WindowModel implements ActionListener {
	
	public static final String SPACECRAFT_TOGGLE_COMMAND = "toggle_spacecraft";
	public static final String CELESTIAL_TOGGLE_COMMAND = "toggle_celestial";
	public static final String POINT_TOGGLE_COMMAND = "toggle_point";

    private MovingObjectsModel movingObjectsModel;
    
    private final ButtonModel spacecraftVisible; 
    private final ButtonModel celestialVisible;
    private final ButtonModel pointsVisible;
    
    private static WindowModel instance = new WindowModel(); 
    
    public static WindowModel getInstance() {
		return instance;
	}

	private WindowModel() {
		spacecraftVisible = new JToggleButton.ToggleButtonModel();
		spacecraftVisible.setSelected(true);
		spacecraftVisible.setActionCommand(SPACECRAFT_TOGGLE_COMMAND);
		spacecraftVisible.addActionListener(this);
		
		celestialVisible = new JToggleButton.ToggleButtonModel();
		celestialVisible.setSelected(true);
		celestialVisible.setActionCommand(CELESTIAL_TOGGLE_COMMAND);
		celestialVisible.addActionListener(this);
		
		pointsVisible = new JToggleButton.ToggleButtonModel();
		pointsVisible.setSelected(true);
		pointsVisible.setActionCommand(POINT_TOGGLE_COMMAND);
		pointsVisible.addActionListener(this);
		
		movingObjectsModel = new MovingObjectsModel(selectMovingObjects());
		movingObjectsModel.setSelectedItem(ModelHolder.getModel().getSelectedObject());
	}
	
	public MovingObjectsModel getMovingObjectsModel() {
		return movingObjectsModel;
	}
	
    /**
     * Returns the celestial objects
     * @param onlyMoving if true only moving objects are returned
     * @return the list of celesial bodies
     */
    public List<CelestialBody> findCelestialBodies(boolean onlyMoving) {
    	List<CelestialBody> list = new ArrayList<>();
        for (MovingObject mo : ModelHolder.getModel().getMovingObjects()) {
	        if (mo instanceof CelestialBody) {
	        	CelestialBody cb = (CelestialBody) mo;
	        	if (!onlyMoving || !cb.getTrajectory().getType().equals(TrajectoryType.STATIC)) {
	        		list.add(cb);
	        	}
	        }
        }
        list = sortNamedObjects(list);
        return list;
    }
    
    public List<PositionProvider> selectMovingObjects() {
    	List<PositionProvider> list = RendererModel.getInstance().findAllPositionProviders();
    	List<PositionProvider> result = new ArrayList<>();
    	for(PositionProvider positionProvider : list) {
    		if (pointsVisible.isSelected() && positionProvider instanceof AbstractOrbitalPoint) {
    			AbstractOrbitalPoint orbitalPoint = (AbstractOrbitalPoint) positionProvider;
    			if (orbitalPoint.isVisible()) {
    				result.add(positionProvider);
    			}
    		}
    		if (spacecraftVisible.isSelected() && positionProvider instanceof Spacecraft) {
    			result.add(positionProvider);
    		}
    		if (celestialVisible.isSelected() && positionProvider instanceof CelestialBody) {
    			result.add(positionProvider);
    		}
    	}
    	result = sortNamedObjects(result);
    	return result;
    }    
    
    protected <T extends PositionProvider> List<T> sortNamedObjects(List<T> list) {
        Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return o1.getName().compareTo(o2.getName());
			}
        });
        return list;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	List<PositionProvider> newItems = selectMovingObjects();
    	movingObjectsModel.removeAllElements();
    	movingObjectsModel.addElements(newItems);
    }
    
    public void setSelectedItem(PositionProvider positionProvider) {
    	movingObjectsModel.setSelectedItem(positionProvider);
    }
    
    public ButtonModel getCelestialVisible() {
		return celestialVisible;
	}
    
    public ButtonModel getPointsVisible() {
		return pointsVisible;
	}
    
    public ButtonModel getSpacecraftVisible() {
		return spacecraftVisible;
	}

}
