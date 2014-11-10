package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.service.ManeuverService;
import com.momega.spacesimulator.service.TargetService;
import com.momega.spacesimulator.service.UserPointService;
import com.momega.spacesimulator.swing.MovingObjectsModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The renderer model
 * Created by martin on 6/20/14.
 */
public class RendererModel {

    private static final Logger logger = LoggerFactory.getLogger(RendererModel.class);

    public final static int MIN_TARGET_SIZE = 9;
    
    public static final double FOVY = 45.0;    

    private static RendererModel instance = new RendererModel();

    private final Map<PositionProvider, ViewCoordinates> viewData = new HashMap<>();

    private final java.util.List<ModelChangeListener> modelChangeListeners = new ArrayList<>();
    
    private MovingObjectsModel movingObjectsModel;

    private final ManeuverService maneuverService;

    private final UserPointService userPointService;

    private final TargetService targetService;
    
    private boolean spacecraftVisible; 
    private boolean celestialVisible;
    private boolean historyPointsVisible;
    private boolean pointsVisible;

    private boolean takeScreenshot = false;



    private Point mouseCoordinates = null;
    private UserOrbitalPoint selectedUserOrbitalPoint;
    private Point draggedPoint;

    private RendererModel() {
        super();
        
        spacecraftVisible = true;
		celestialVisible = true;
		pointsVisible = true;
        historyPointsVisible = true;
        maneuverService = Application.getInstance().getService(ManeuverService.class);
        userPointService = Application.getInstance().getService(UserPointService.class);
        targetService = Application.getInstance().getService(TargetService.class);
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
    
    public List<Spacecraft> findAllSpacecrafs() {
    	List<Spacecraft> result = new ArrayList<>();
    	for(MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
    		if (dp instanceof Spacecraft) {
    			result.add((Spacecraft) dp);
    		}
    	}
    	return result;
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
                for(OrbitIntersection intersection : targetService.getOrbitIntersections(spacecraft)) {
                	result.add(intersection);
                }
                for(ManeuverPoint maneuverPoint : maneuverService.findActiveOrNextPoints(spacecraft, ModelHolder.getModel().getTime())) {
                    result.add(maneuverPoint);
                }
                for(UserOrbitalPoint userOrbitalPoint : spacecraft.getUserOrbitalPoints()) {
                    result.add(userOrbitalPoint);
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
        GL2 gl = drawable.getGL().getGL2();
        ScreenCoordinates screenCoordinates = GLUtils.getProjectionCoordinates(gl, positionProvider.getPosition(), camera);
        viewCoordinates.setVisible(screenCoordinates != null);
        viewCoordinates.setScreenCoordinates(screenCoordinates);
        double radiusAngle;
        if (positionProvider instanceof RotatingObject) {
            RotatingObject ro = (RotatingObject) positionProvider;
            Vector3d distance = positionProvider.getPosition().subtract(camera.getPosition());
            radiusAngle = Math.toDegrees(Math.atan2(ro.getRadius(), distance.length()));
            double radius = (int)((radiusAngle/ FOVY) * drawable.getSurfaceHeight());
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
    	return new ArrayList<>(viewData.keySet());
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
    
    public Spacecraft findSpacecraftByIndex(int index) {
    	Assert.isTrue(index>0);
    	for(MovingObject movingObject : ModelHolder.getModel().getMovingObjects()) {
            if (movingObject instanceof Spacecraft) {
                Spacecraft spacecraft = (Spacecraft) movingObject;
                if (spacecraft.getIndex()==index) {
                	return spacecraft;
                }
            }
    	}
        return null;
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

    public void setMouseCoordinates(Point mouseCoordinates) {
        this.mouseCoordinates = mouseCoordinates;
    }

    public Point getMouseCoordinates() {
        return mouseCoordinates;
    }

    public void createUserPoint(GLAutoDrawable drawable, Point point) {
        GL2 gl = drawable.getGL().getGL2();
        Map<Integer, ScreenCoordinates> screenCoordinatesMap = GLUtils.getStencilPosition(gl, point, RendererModel.MIN_TARGET_SIZE);
        if (screenCoordinatesMap.size()==1) { // only one object is selected
        	Map.Entry<Integer, ScreenCoordinates> entry = screenCoordinatesMap.entrySet().iterator().next();
            ScreenCoordinates screenCoordinates = screenCoordinatesMap.values().iterator().next();

            Vector3d modelCoordinates = GLUtils.getModelCoordinates(gl, screenCoordinates);
            logger.info("model coordinates = {}", modelCoordinates.asArray());

            Spacecraft spacecraft = RendererModel.getInstance().findSpacecraftByIndex(entry.getKey().intValue());
            UserPointService userPointService = Application.getInstance().getService(UserPointService.class);
            userPointService.createUserOrbitalPoint(spacecraft, modelCoordinates);
        }
    }

    public void dragUserPoint(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        Map<Integer, ScreenCoordinates> screenCoordinatesMap = GLUtils.getStencilPosition(gl, draggedPoint, RendererModel.MIN_TARGET_SIZE);
        if (screenCoordinatesMap.size()==1) { // only one object is selected
            ScreenCoordinates screenCoordinates = screenCoordinatesMap.values().iterator().next();
            Vector3d modelCoordinates = GLUtils.getModelCoordinates(gl, screenCoordinates);

            logger.info("dragged model coordinates = {}", modelCoordinates.asArray());
            userPointService.updateUserOrbitalPoint(selectedUserOrbitalPoint, modelCoordinates);
        }
    }

    public void setSelectedUserOrbitalPoint(UserOrbitalPoint selectedUserOrbitalPoint) {
        this.selectedUserOrbitalPoint = selectedUserOrbitalPoint;
    }

    public UserOrbitalPoint getSelectedUserOrbitalPoint() {
        return selectedUserOrbitalPoint;
    }

    public void setDraggedPoint(Point draggedPoint) {
        this.draggedPoint = draggedPoint;
    }

    public Point getDraggedPoint() {
        return draggedPoint;
    }
}
