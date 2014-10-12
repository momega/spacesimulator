package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.service.ManeuverService;
import com.momega.spacesimulator.swing.MovingObjectsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GLAutoDrawable;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by martin on 6/20/14.
 */
public class RendererModel {

    private static final Logger logger = LoggerFactory.getLogger(RendererModel.class);

    public final static int MIN_TARGET_SIZE = 5;
    
    public static final double FOVY = 45.0;    

    private static RendererModel instance = new RendererModel();

    private final Map<PositionProvider, ViewCoordinates> viewData = new HashMap<>();

    private final java.util.List<ModelChangeListener> modelChangeListeners = new ArrayList<>();
    
    private MovingObjectsModel movingObjectsModel;

    private final ManeuverService maneuverService;
    
    private boolean spacecraftVisible; 
    private boolean celestialVisible;
    private boolean historyPointsVisible;
    private boolean pointsVisible;

    private boolean takeScreenshot = false;
    

    private RendererModel() {
        super();
        
        spacecraftVisible = true;
		celestialVisible = true;
		pointsVisible = true;
        historyPointsVisible = true;
        maneuverService = Application.getInstance().getService(ManeuverService.class);

		movingObjectsModel = new MovingObjectsModel(selectMovingObjects());
		movingObjectsModel.setSelectedItem(ModelHolder.getModel().getSelectedObject());


    }

    public static RendererModel getInstance() {
        return instance;
    }

    public void addModelChangeListener(ModelChangeListener listener) {
        modelChangeListeners.add(listener);
    }

    public void removeModelChangeListener(ModelChangeListener listener) {
        modelChangeListeners.remove(listener);
    }
    
    public List<PositionProvider> findAllPositionProviders() {
    	List<PositionProvider> result = new ArrayList<>();
    	for(MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
            result.add(dp);
            KeplerianTrajectory keplerianTrajectory = dp.getTrajectory();
            if (dp instanceof CelestialBody || dp instanceof BaryCentre || dp instanceof Spacecraft) {
                result.add(keplerianTrajectory.getApoapsis());
                result.add(keplerianTrajectory.getPeriapsis());
            }

            if (dp instanceof Spacecraft) {
                Spacecraft spacecraft = (Spacecraft) dp;
                for(HistoryPoint hp : spacecraft.getHistoryTrajectory().getNamedHistoryPoints()) {
                    result.add(hp);
                }
                for(OrbitIntersection intersection : spacecraft.getOrbitIntersections()) {
                	result.add(intersection);
                }
                for(ManeuverPoint maneuverPoint : maneuverService.findActiveOrNextPoints(spacecraft, ModelHolder.getModel().getTime())) {
                    result.add(maneuverPoint);
                }
            }
        }
    	return result;
    }
    
    public void updateViewData(GLAutoDrawable drawable) {
    	Camera camera = ModelHolder.getModel().getCamera();
        for(PositionProvider positionProvider : findAllPositionProviders()) {
            addViewCoordinates(drawable, positionProvider, camera);
        }
    }
    
    protected void addViewCoordinates(GLAutoDrawable drawable, PositionProvider positionProvider, Camera camera) {
        if (positionProvider == null) {
            return;
        }

        ViewCoordinates viewCoordinates = new ViewCoordinates();
        Point point = GLUtils.getProjectionCoordinates(drawable, positionProvider.getPosition(), camera);
        viewCoordinates.setVisible(point != null);
        viewCoordinates.setPoint(point);
        double radiusAngle;
        if (positionProvider instanceof RotatingObject) {
            RotatingObject ro = (RotatingObject) positionProvider;
            Vector3d distance = positionProvider.getPosition().subtract(camera.getPosition());
            radiusAngle = Math.toDegrees(Math.atan2(ro.getRadius(), distance.length()));
            double radius = (int)((radiusAngle/ FOVY) * drawable.getHeight());
            viewCoordinates.setRadius(radius);
        } else {
            viewCoordinates.setRadius(MIN_TARGET_SIZE);
        }

        if (positionProvider instanceof AbstractOrbitalPoint) {
        	AbstractOrbitalPoint apsis = (AbstractOrbitalPoint) positionProvider;
            viewCoordinates.setVisible(viewCoordinates.isVisible() && apsis.isVisible());
        }

        viewCoordinates.setObject(positionProvider);
        RendererModel.getInstance().addViewCoordinates(viewCoordinates);
    }    

    public void modelChanged() {
        Model model = ModelHolder.getModel();
        ModelChangeEvent event = new ModelChangeEvent(model);
        for(ModelChangeListener listener : modelChangeListeners) {
            listener.modelChanged(event);
        }
    }

    /**
     * Adds the view coordinates the renderer model for the given dynamic point
     * @param viewCoordinates the view coordinates
     */
    public void addViewCoordinates(ViewCoordinates viewCoordinates) {
        viewData.put(viewCoordinates.getObject(), viewCoordinates);
    }

    public ViewCoordinates findViewCoordinates(PositionProvider positionProvider) {
        return viewData.get(positionProvider);
    }
    
    public List<PositionProvider> getAll() {
    	List<PositionProvider> result = new ArrayList<>(viewData.keySet());
    	return result;
    }

    public boolean isVisibleOnScreen(PositionProvider positionProvider) {
    	if (positionProvider == null) {
    		return false;
    	}
        ViewCoordinates viewCoordinates = findViewCoordinates(positionProvider);
        return (viewCoordinates != null && viewCoordinates.isVisible());
    }

    public List<ViewCoordinates> findViewCoordinates(java.awt.Point point) {
        double x = point.getX();
        double y = point.getY();
        List<ViewCoordinates> result = new ArrayList<>();
        for (Map.Entry<PositionProvider, ViewCoordinates> entry : viewData.entrySet()) {
            ViewCoordinates viewCoordinates = entry.getValue();
            if (viewCoordinates.isVisible()) {
                if ((Math.abs(x - (int) viewCoordinates.getPoint().getX()) < MIN_TARGET_SIZE) && (Math.abs(y - (int) viewCoordinates.getPoint().getY()) < MIN_TARGET_SIZE)) {
                    result.add(viewCoordinates);
                }
            }
        }
        return result;
    }

    public ViewCoordinates findByName(String name) {
        if (name == null) {
            return null;
        }
        for (Map.Entry<PositionProvider, ViewCoordinates> entry : viewData.entrySet()) {
            ViewCoordinates viewCoordinates = entry.getValue();
            if (name.equals(viewCoordinates.getObject().getName())) {
                return viewCoordinates;
            }
        }
        return null;
    }
 
    public String[] findVisibleObjects() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<PositionProvider, ViewCoordinates> entry : viewData.entrySet()) {
            ViewCoordinates viewCoordinates = entry.getValue();
            if (viewCoordinates.isVisible()) {
                list.add(viewCoordinates.getObject().getName());
            }
        }
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }

    public void selectItem(ViewCoordinates viewCoordinates) {
        Model model = ModelHolder.getModel();
        Camera camera = model.getCamera();
        camera.setTargetObject(viewCoordinates.getObject());
        if (viewCoordinates.getObject() instanceof RotatingObject) {
            RotatingObject ro = (RotatingObject) viewCoordinates.getObject();
            if (camera.getDistance() < ro.getRadius()) {
                camera.setDistance(ro.getRadius() * 10);
            }
        }
        model.setSelectedObject(viewCoordinates.getObject());
        setSelectedItem(viewCoordinates.getObject());
        logger.info("selected dynamical point changed to {}", viewCoordinates.getObject().getName());
    }
    
    public void setSelectedItem(PositionProvider positionProvider) {
    	movingObjectsModel.setSelectedItem(positionProvider);
    }

	public void clearViewCoordinates() {
		viewData.clear();
	}
    
    public boolean isCelestialVisible() {
		return celestialVisible;
	}
    
    public boolean isPointsVisible() {
		return pointsVisible;
	}

    public boolean isHistoryPointsVisible() {
        return historyPointsVisible;
    }

    public boolean isSpacecraftVisible() {
		return spacecraftVisible;
	}
    
    public void setPointsVisible(boolean pointsVisible) {
		this.pointsVisible = pointsVisible;
	}

    public void setHistoryPointsVisible(boolean historyPointsVisible) {
        this.historyPointsVisible = historyPointsVisible;
    }

    public void setSpacecraftVisible(boolean spacecraftVisible) {
		this.spacecraftVisible = spacecraftVisible;
	}
    
    public void setCelestialVisible(boolean celestialVisible) {
		this.celestialVisible = celestialVisible;
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
    	List<PositionProvider> list = findAllPositionProviders();
    	List<PositionProvider> result = new ArrayList<>();
    	for(PositionProvider positionProvider : list) {
    		if (isPointsVisible() && positionProvider instanceof AbstractOrbitalPoint) {
    			AbstractOrbitalPoint orbitalPoint = (AbstractOrbitalPoint) positionProvider;
    			if (orbitalPoint.isVisible()) {
    				result.add(positionProvider);
    			}
    		}
    		if (isHistoryPointsVisible() && positionProvider instanceof HistoryPoint) {
    			result.add(positionProvider);
    		}
    		if (isSpacecraftVisible() && positionProvider instanceof Spacecraft) {
    			result.add(positionProvider);
    		}
    		if (isCelestialVisible() && positionProvider instanceof CelestialBody) {
    			result.add(positionProvider);
    		}
    	}
    	result = sortNamedObjects(result);
    	return result;
    }

    public boolean isTakeScreenshot() {
        return takeScreenshot;
    }

    public void setTakeScreenshot(boolean takeScreenshot) {
        this.takeScreenshot = takeScreenshot;
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

}
