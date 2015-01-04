package com.momega.spacesimulator.renderer;

import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.AbstractOrbitalPoint;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.RotatingObject;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.UserOrbitalPoint;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.service.ModelSerializer;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.service.UserPointService;
import com.momega.spacesimulator.swing.PositionProvidersModel;

/**
 * The renderer model
 * Created by martin on 6/20/14.
 */
public class RendererModel {

	public static final String MODEL_FILE = "modelFile";
	public static final String WARP_FACTOR = "warpFactor";
    public static final String FPS = "fps";
    public static final String MODEL_READY = "modelReady";

	private static final Logger logger = LoggerFactory.getLogger(RendererModel.class);

    public final static int MIN_TARGET_SIZE = 9;
    
    public static final double FOVY = 45.0;    

    private static RendererModel instance = new RendererModel();

    private final Map<PositionProvider, ViewCoordinates> viewData = new HashMap<>();

    private final java.util.List<ModelChangeListener> modelChangeListeners = new ArrayList<>();
    
    private PositionProvidersModel movingObjectsModel;

    private final UserPointService userPointService;
    private final ModelService modelService;
    
    private boolean spacecraftVisible; 
    private boolean celestialVisible;
    private boolean historyPointsVisible;
    private boolean pointsVisible;
    
    private File modelFile;
    private final JFileChooser fileChooser;

	private UserOrbitalPoint selectedUserOrbitalPoint;
	private PropertyChangeSupport propertyChangeSupport;

	private double warpFactor = 0.1;
	
	private Spacecraft newSpacecraft = null;
	private Spacecraft deleteSpacecraft = null;
    private boolean reloadRenderersRequired = false;
    private boolean takeScreenshotRequired = false;
    private Point newUserPointPosition = null;
    private Point dragUserPointPosition = null;
    private File saveFileRequested = null;
    private File loadFileRequested = null;
    private boolean quitRequested = false;
    private boolean closeRequested = false;

    private boolean modelReady = false;
    
    private final ModelSerializer modelSerializer;
    private float fps = 0.0f;
    private ModelBuilder modelBuilderRequested;

    private RendererModel() {
        super();
        
        spacecraftVisible = true;
		celestialVisible = true;
		pointsVisible = true;
        historyPointsVisible = true;
        userPointService = Application.getInstance().getService(UserPointService.class);
        modelService = Application.getInstance().getService(ModelService.class);
		movingObjectsModel = new PositionProvidersModel(selectPositionProviders());
        if (ModelHolder.getModel()!=null) {
		    movingObjectsModel.setSelectedItem(ModelHolder.getModel().getCamera().getTargetObject());
        }
		fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Space Simulator Data (.json)", "json"));
		fileChooser.setFileFilter(fileChooser.getChoosableFileFilters()[1]);
		modelSerializer = Application.getInstance().getService(ModelSerializer.class);
		
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

    public void updateViewData(GLAutoDrawable drawable) {
    	Camera camera = ModelHolder.getModel().getCamera();
        for(PositionProvider positionProvider : modelService.findAllPositionProviders()) {
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
        addViewCoordinates(viewCoordinates);
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

    public PositionProvider findByName(String name) {
        if (name == null) {
            return null;
        }
        for (Map.Entry<PositionProvider, ViewCoordinates> entry : viewData.entrySet()) {
            ViewCoordinates viewCoordinates = entry.getValue();
            if (name.equals(viewCoordinates.getObject().getName())) {
                return viewCoordinates.getObject();
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
        if (isModelReady()) {
            movingObjectsModel.setSelectedItem(ModelHolder.getModel().getCamera().getTargetObject());
        }
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

    public List<PositionProvider> selectPositionProviders() {
    	List<PositionProvider> list = modelService.findAllPositionProviders();
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
    	result = modelService.sortNamedObjects(result);
    	return result;
    }

    public void createUserPoint(GLAutoDrawable drawable, Point point) {
        GL2 gl = drawable.getGL().getGL2();
        Map<Integer, ScreenCoordinates> screenCoordinatesMap = GLUtils.getStencilPosition(gl, point, RendererModel.MIN_TARGET_SIZE);
        if (screenCoordinatesMap.size()==1) { // only one object is selected
        	Map.Entry<Integer, ScreenCoordinates> entry = screenCoordinatesMap.entrySet().iterator().next();
            ScreenCoordinates screenCoordinates = screenCoordinatesMap.values().iterator().next();

            Vector3d modelCoordinates = GLUtils.getModelCoordinates(gl, screenCoordinates);
            logger.info("model coordinates = {}", modelCoordinates.asArray());

            MovingObject movingObject = modelService.findMovingObjectByIndex(entry.getKey().intValue());
            UserPointService userPointService = Application.getInstance().getService(UserPointService.class);
            userPointService.createUserOrbitalPoint(movingObject, modelCoordinates);
        }
    }

    public void dragUserPoint(GLAutoDrawable drawable, Point draggedPoint) {
        GL2 gl = drawable.getGL().getGL2();
        UserOrbitalPoint userOrbitalPoint = getSelectedUserOrbitalPoint();
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


    public void setWarpFactor(double warpFactor) {
    	double oldValue = this.warpFactor;
        this.warpFactor = warpFactor;
        firePropertyChange(WARP_FACTOR, oldValue, warpFactor);
    }

    public double getWarpFactor() {
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

    public boolean isReloadRenderersRequired() {
        return reloadRenderersRequired;
    }

    public void setReloadRenderersRequired(boolean reloadRenderersRequired) {
        this.reloadRenderersRequired = reloadRenderersRequired;
    }

    public boolean isTakeScreenshotRequired() {
        return takeScreenshotRequired;
    }

    public void setTakeScreenshotRequired(boolean takeScreenshotRequired) {
        this.takeScreenshotRequired = takeScreenshotRequired;
    }
    
    public void setNewUserPointPosition(Point newUserPointPosition) {
		this.newUserPointPosition = newUserPointPosition;
	}
    
    public Point getNewUserPointPosition() {
		return newUserPointPosition;
	}
    
    public void setSaveFileRequested(File saveFileRequested) {
		this.saveFileRequested = saveFileRequested;
}
    
    public File getSaveFileRequested() {
		return saveFileRequested;
	}
    
    public File getLoadFileRequested() {
		return loadFileRequested;
	}
    
    public void setLoadFileRequested(File loadFileRequested) {
		this.loadFileRequested = loadFileRequested;
	}
    
    public void setDragUserPointPosition(Point dragUserPointPosition) {
		this.dragUserPointPosition = dragUserPointPosition;
	}
    
    public Point getDragUserPointPosition() {
		return dragUserPointPosition;
	}
    
    public void setQuitRequested(boolean quitRequested) {
		this.quitRequested = quitRequested;
	}
    
    public boolean isQuitRequested() {
		return quitRequested;
	}
    
    public void doSave(boolean saveAs) {
    	File file = null;
    	if (!saveAs) {
    		file = getModelFile();
    	}
		if (file == null) {
			file = selectSaveFile();
		}
		if (file != null) {
			setSaveFileRequested(file);
		}
    }
    
    public void saveFile(File file) {
		logger.info("file = {}", file);
		FileWriter fileWriter = null;
		Assert.notNull(file);
		try {
			fileWriter = new FileWriter(file);
			modelSerializer.save(ModelHolder.getModel(), fileWriter);
			fileWriter.flush();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					StatusBarEvent event = new StatusBarEvent(ModelHolder.getModel(), "Model successfully saved.");
					fireModelEvent(event);

				}
			});
			setModelFile(file);
		} catch (final IOException ioe) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					JOptionPane.showMessageDialog(null,
						    ioe.getMessage(),
						    "Save Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			});
		} finally {
			IOUtils.closeQuietly(fileWriter);
		}
	}	 
    
	public Model loadFile(File file) {
		FileReader fileReader = null;
		Model model = null;
		try {
			fileReader = new FileReader(file);
			model = modelSerializer.load(fileReader);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					StatusBarEvent event = new StatusBarEvent(ModelHolder.getModel(), "Model successfully loaded.");
					fireModelEvent(event);
				}
			});
			setModelFile(file);
		} catch (final IOException ioe) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					JOptionPane.showMessageDialog(null,
						    ioe.getMessage(),
						    "Load Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			});
		} finally {
			IOUtils.closeQuietly(fileReader);
		}
		return model;
	}
	
	public File selectSaveFile() {
		JFileChooser fileChooser = getFileChooser();
		fileChooser.setDialogTitle("Save Dialog...");
		if (fileChooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

    public float getFps() {
        return this.fps;
    }

    public void setFps(float fps) {
        float old = this.fps;
        this.fps = fps;
        firePropertyChange(FPS, old, this.fps);
    }

    public void setModelReady(boolean modelReady) {
        boolean old = this.modelReady;
        this.modelReady = modelReady;
        firePropertyChange(MODEL_READY, old, this.modelReady);
    }

    public boolean isModelReady() {
        return modelReady;
    }

    public void setCloseRequested(boolean closeRequested) {
        this.closeRequested = closeRequested;
    }

    public boolean isCloseRequested() {
        return closeRequested;
    }

    public void setModelBuilderRequested(ModelBuilder modelBuilderRequested) {
        this.modelBuilderRequested = modelBuilderRequested;
    }

    public ModelBuilder getModelBuilderRequested() {
        return modelBuilderRequested;
    }

    public void createFromBuilder(ModelBuilder modelBuilder) {
        Application.getInstance().init(modelBuilder);
        setModelReady(true);

        fireModelEvent(new StatusBarEvent(ModelHolder.getModel(), "Model created from builder '" + modelBuilder.getName() + "'"));
    }

    public void createFromBuilder() {
        createFromBuilder(getModelBuilderRequested());
    }
    
    public void removeSpacecraft(Spacecraft spacecraft) {
    	modelService.removeMovingObject(spacecraft);
    }
}
