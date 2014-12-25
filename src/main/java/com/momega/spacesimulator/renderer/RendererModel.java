package com.momega.spacesimulator.renderer;

import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.AbstractOrbitalPoint;
import com.momega.spacesimulator.model.BaryCentre;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.KeplerianTrajectory;
import com.momega.spacesimulator.model.ManeuverPoint;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.OrbitIntersection;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.RotatingObject;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.UserOrbitalPoint;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.service.ManeuverService;
import com.momega.spacesimulator.service.TargetService;
import com.momega.spacesimulator.service.UserPointService;
import com.momega.spacesimulator.swing.PositionProvidersModel;

/**
 * The renderer model
 * Created by martin on 6/20/14.
 */
public class RendererModel {

	public static final String MODEL_FILE = "modelFile";
	public static final String WARP_FACTOR = "warpFactor";

	private static final Logger logger = LoggerFactory.getLogger(RendererModel.class);

    public final static int MIN_TARGET_SIZE = 9;
    
    public static final double FOVY = 45.0;    

    private static RendererModel instance = new RendererModel();

    private final Map<PositionProvider, ViewCoordinates> viewData = new HashMap<>();

    private final java.util.List<ModelChangeListener> modelChangeListeners = new ArrayList<>();
    
    private PositionProvidersModel movingObjectsModel;

    private final ManeuverService maneuverService;
    private final UserPointService userPointService;
    private final TargetService targetService;
    
    private boolean spacecraftVisible; 
    private boolean celestialVisible;
    private boolean historyPointsVisible;
    private boolean pointsVisible;
    
    private File modelFile;
    private final JFileChooser fileChooser;
    private boolean reloadModelRequested;

	private UserOrbitalPoint selectedUserOrbitalPoint;
	private PropertyChangeSupport propertyChangeSupport;

	private BigDecimal warpFactor = BigDecimal.valueOf(0.1);
	
	private Spacecraft newSpacecraft = null;
	private Spacecraft deleteSpacecraft = null;

    private RendererModel() {
        super();
        
        spacecraftVisible = true;
		celestialVisible = true;
		pointsVisible = true;
        historyPointsVisible = true;
        maneuverService = Application.getInstance().getService(ManeuverService.class);
        userPointService = Application.getInstance().getService(UserPointService.class);
        targetService = Application.getInstance().getService(TargetService.class);
		movingObjectsModel = new PositionProvidersModel(selectPositionProviders());
		movingObjectsModel.setSelectedItem(ModelHolder.getModel().getCamera().getTargetObject());
		fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Space Simulator Data (.json)", "json"));
		fileChooser.setFileFilter(fileChooser.getChoosableFileFilters()[1]);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
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
    
    public List<CelestialBody> findAllCelestialBodies() {
    	List<CelestialBody> result = new ArrayList<>();
    	for(MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
    		if (dp instanceof CelestialBody) {
    			result.add((CelestialBody) dp);
    		}
    	}
    	return result;
    }
    
    public List<Planet> findAllPlanets() {
    	List<Planet> result = new ArrayList<>();
    	for(MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
    		if (dp instanceof Planet) {
    			result.add((Planet) dp);
    		}
    	}
    	return result;
    }
    
    public List<MovingObject> findAllMovingObjects() {
    	List<MovingObject> result = new ArrayList<>();
    	for(MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
   			result.add(dp);
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
                MovingObject body = (MovingObject) dp;
            	for(UserOrbitalPoint userOrbitalPoint : body.getUserOrbitalPoints()) {
                    result.add(userOrbitalPoint);
                }
            }

            if (dp instanceof Spacecraft) {
                Spacecraft spacecraft = (Spacecraft) dp;
                for(HistoryPoint hp : spacecraft.getNamedHistoryPoints()) {
                    result.add(hp);
                }
                for(OrbitIntersection intersection : targetService.getOrbitIntersections(spacecraft)) {
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
        
        if (positionProvider.getPosition() == null) {
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
        	AbstractOrbitalPoint point = (AbstractOrbitalPoint) positionProvider;
            viewCoordinates.setVisible(viewCoordinates.isVisible() && point.isVisible());
        }

        viewCoordinates.setObject(positionProvider);
        RendererModel.getInstance().addViewCoordinates(viewCoordinates);
    }    

    public void modelChanged() {
        Model model = ModelHolder.getModel();
        ModelChangeEvent event = new ModelChangeEvent(model);
        fireModelEvent(event);
    }
    
    public void fireModelEvent(ModelChangeEvent event) {
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
    
    public MovingObject findMovingObjectByIndex(int index) {
    	Assert.isTrue(index>0);
    	for(MovingObject movingObject : ModelHolder.getModel().getMovingObjects()) {
            MovingObject body = (MovingObject) movingObject;
            if (body.getIndex()==index) {
            	return body;
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
        setSelectedItem(viewCoordinates.getObject());
        logger.info("selected dynamical point changed to {}", viewCoordinates.getObject().getName());
    }
    
    public void setSelectedItem(PositionProvider positionProvider) {
    	movingObjectsModel.setSelectedItem(positionProvider);
    }
    
    public void replaceMovingObjectsModel() {
    	movingObjectsModel.setSelectedItem(null);
    	movingObjectsModel.replaceElements(selectPositionProviders());
    	movingObjectsModel.setSelectedItem(ModelHolder.getModel().getCamera().getTargetObject());
    }
    
    public PositionProvider getSelectedItem() {
    	return (PositionProvider) movingObjectsModel.getSelectedItem();
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
    
    public PositionProvidersModel getMovingObjectsModel() {
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
	        	if (!onlyMoving || !cb.isStatic()) {
	        		list.add(cb);
	        	}
	        }
        }
        list = sortNamedObjects(list);
        return list;
    }
    
    public List<PositionProvider> selectPositionProviders() {
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

    protected <T extends PositionProvider> List<T> sortNamedObjects(List<T> list) {
        Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return o1.getName().compareTo(o2.getName());
			}
        });
        return list;
    }

    public void createUserPoint(GLAutoDrawable drawable, Point point) {
        GL2 gl = drawable.getGL().getGL2();
        Map<Integer, ScreenCoordinates> screenCoordinatesMap = GLUtils.getStencilPosition(gl, point, RendererModel.MIN_TARGET_SIZE);
        if (screenCoordinatesMap.size()==1) { // only one object is selected
        	Map.Entry<Integer, ScreenCoordinates> entry = screenCoordinatesMap.entrySet().iterator().next();
            ScreenCoordinates screenCoordinates = screenCoordinatesMap.values().iterator().next();

            Vector3d modelCoordinates = GLUtils.getModelCoordinates(gl, screenCoordinates);
            logger.info("model coordinates = {}", modelCoordinates.asArray());

            MovingObject movingObject = RendererModel.getInstance().findMovingObjectByIndex(entry.getKey().intValue());
            UserPointService userPointService = Application.getInstance().getService(UserPointService.class);
            userPointService.createUserOrbitalPoint(movingObject, modelCoordinates);
        }
    }

    public void dragUserPoint(GLAutoDrawable drawable, UserOrbitalPoint userOrbitalPoint, Point draggedPoint) {
        GL2 gl = drawable.getGL().getGL2();
        Map<Integer, ScreenCoordinates> screenCoordinatesMap = GLUtils.getStencilPosition(gl, draggedPoint, RendererModel.MIN_TARGET_SIZE);
        if (screenCoordinatesMap.size()==1) { // only one object is selected
            ScreenCoordinates screenCoordinates = screenCoordinatesMap.values().iterator().next();
            Vector3d modelCoordinates = GLUtils.getModelCoordinates(gl, screenCoordinates);

            logger.info("dragged model coordinates = {}", modelCoordinates.asArray());
            userPointService.updateUserOrbitalPoint(userOrbitalPoint, modelCoordinates);
        }
    }

    public void setSelectedUserOrbitalPoint(UserOrbitalPoint selectedUserOrbitalPoint) {
        this.selectedUserOrbitalPoint = selectedUserOrbitalPoint;
    }

    public UserOrbitalPoint getSelectedUserOrbitalPoint() {
        return selectedUserOrbitalPoint;
    }
    
    public File getModelFile() {
		return modelFile;
	}
    
    public void setModelFile(File modelFile) {
		File oldFile = this.modelFile;
		this.modelFile = modelFile;
		firePropertyChange(MODEL_FILE, oldFile, this.modelFile);
	}
	
	public JFileChooser getFileChooser() {
		return fileChooser;
	}

	public boolean isReloadModelRequested() {
		return reloadModelRequested;
	}
	
	public void setReloadModelRequested(boolean reloadModelRequested) {
		this.reloadModelRequested = reloadModelRequested;
	}
	
	public void addPropertyChangeListener(String propertyName, final PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, final PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
	}

	protected void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	public void initPropertyChange(final String propertyName, final Object newValue) {
		firePropertyChange(propertyName, null, newValue);
	}
	
	public List<Integer> getAvailableIndexes() {
		List<Integer> result = new ArrayList<>();
		for(int i=1; i<=9; i++) {
			result.add(Integer.valueOf(i));
		}
		for(MovingObject mo : ModelHolder.getModel().getMovingObjects()) {
			result.remove(mo.getIndex());
		}
		return result;
	}	


    public void setWarpFactor(BigDecimal warpFactor) {
    	BigDecimal oldValue = this.warpFactor;
        this.warpFactor = warpFactor;
        firePropertyChange(WARP_FACTOR, oldValue, warpFactor);
    }

    public BigDecimal getWarpFactor() {
        return warpFactor;
    }
    
    public void setNewSpacecraft(Spacecraft newSpacecraft) {
		this.newSpacecraft = newSpacecraft;
	}
    
    public Spacecraft getNewSpacecraft() {
		return newSpacecraft;
	}
    
    public void setDeleteSpacecraft(Spacecraft deleteSpacecraft) {
		this.deleteSpacecraft = deleteSpacecraft;
	}
    
    public Spacecraft getDeleteSpacecraft() {
		return deleteSpacecraft;
	}
	
}
