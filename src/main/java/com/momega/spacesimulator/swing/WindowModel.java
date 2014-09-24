/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
public class WindowModel {
	

    private MovingObjectsModel movingObjectsModel;
    
    private boolean spacecraftVisible; 
    private boolean celestialVisible;
    private boolean pointsVisible;
    
    private static WindowModel instance = new WindowModel(); 
    
    public static WindowModel getInstance() {
		return instance;
	}

	private WindowModel() {
		spacecraftVisible = true;
		celestialVisible = true;
		pointsVisible = true;
		
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
    		if (pointsVisible && positionProvider instanceof AbstractOrbitalPoint) {
    			AbstractOrbitalPoint orbitalPoint = (AbstractOrbitalPoint) positionProvider;
    			if (orbitalPoint.isVisible()) {
    				result.add(positionProvider);
    			}
    		}
    		if (spacecraftVisible && positionProvider instanceof Spacecraft) {
    			result.add(positionProvider);
    		}
    		if (celestialVisible && positionProvider instanceof CelestialBody) {
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
    
    public void setSelectedItem(PositionProvider positionProvider) {
    	movingObjectsModel.setSelectedItem(positionProvider);
    }
    
    public boolean isCelestialVisible() {
		return celestialVisible;
	}
    
    public boolean isPointsVisible() {
		return pointsVisible;
	}
    
    public boolean isSpacecraftVisible() {
		return spacecraftVisible;
	}
    
    public void setPointsVisible(boolean pointsVisible) {
		this.pointsVisible = pointsVisible;
	}
    
    public void setSpacecraftVisible(boolean spacecraftVisible) {
		this.spacecraftVisible = spacecraftVisible;
	}
    
    public void setCelestialVisible(boolean celestialVisible) {
		this.celestialVisible = celestialVisible;
	}

}
