/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.NamedObject;
import com.momega.spacesimulator.model.TrajectoryType;

/**
 * @author martin
 *
 */
public class WindowModel {

    private MovingObjectsModel movingObjectsModel;
    
    private static WindowModel instance = new WindowModel(); 
    
    public static WindowModel getInstance() {
		return instance;
	}

	private WindowModel() {
		this.movingObjectsModel = new MovingObjectsModel(findMovingObjects());
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
    
    public List<MovingObject> findMovingObjects() {
    	List<MovingObject> list = new ArrayList<>(ModelHolder.getModel().getMovingObjects());
    	list = sortNamedObjects(list);
    	return list;
    }    
    
    protected <T extends NamedObject> List<T> sortNamedObjects(List<T> list) {
        Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return o1.getName().compareTo(o2.getName());
			}
        });
        return list;
    }

}
