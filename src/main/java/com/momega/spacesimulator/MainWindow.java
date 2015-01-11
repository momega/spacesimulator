package com.momega.spacesimulator;

import com.jogamp.newt.event.KeyEvent;
import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.controller.*;
import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.opengl.MainGLRenderer;
import com.momega.spacesimulator.renderer.ModelChangeEvent;
import com.momega.spacesimulator.renderer.ModelChangeListener;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.renderer.StatusBarEvent;
import com.momega.spacesimulator.service.HistoryPointListener;
import com.momega.spacesimulator.service.HistoryPointService;
import com.momega.spacesimulator.swing.*;
import com.momega.spacesimulator.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

/**
 * The main window of the application
 * @author martin
 */
public class MainWindow extends DefaultWindow {

	private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);

	private static final String BUILDER_CLASS_NAME_OPTION = "builderClassName";

	private static final String FILE_NAME_OPTION = "fileName";
	
    public MainWindow(String title) {
        super(title);
    }

    public static void main(String[] args) {
        final MainWindow window = new MainWindow("Space Simulator");
        EventBusController controller = new EventBusController();

        MainGLRenderer mr = new MainGLRenderer(window);
        controller.addController(new QuitController(window));
        controller.addController(new UserPointController());
        controller.addController(new TargetController(window));
        controller.addController(new TakeScreenshotController());
        controller.addController(new CameraController());
        controller.addController(new TimeController(window));
        controller.addController(new PerspectiveController(mr));
        controller.addController(new ToolbarController());
        controller.addController(new PreferencesController());
        controller.addController(new InterplanetaryFlightController());
        controller.addController(new LoadController());
        controller.addController(new SaveController());
        controller.addController(new SpacecraftController());
		controller.addController(new CloseController());
		controller.addController(new CreateFromBuilderController());
        window.openWindow(mr, controller);
        
        final RendererModel rendererModel = RendererModel.getInstance();
        
        rendererModel.addPropertyChangeListener(RendererModel.MODEL_FILE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				File file = (File) evt.getNewValue();
				String title = window.getTitle();
				if (file != null) {
					title += " (" + file.getAbsolutePath() + ")";
				}
				window.getFrame().setTitle(title);
			}
		});

		PropertySource<?> ps = new SimpleCommandLinePropertySource(args);
		String builderClassName = (String) ps.getProperty(BUILDER_CLASS_NAME_OPTION);
		if (builderClassName != null) {
			ModelBuilder modelBuilder = Application.getInstance().createBuilder(builderClassName);
			RendererModel.getInstance().setModelBuilderRequested(modelBuilder);
			return;
		}

		String fileName = (String) ps.getProperty(FILE_NAME_OPTION);
		if (fileName!=null) {
			RendererModel.getInstance().setLoadFileRequested(new File(fileName));
			return;
		}

		if (!rendererModel.isModelReady()) {
			ActionEvent event = new ActionEvent(window, ActionEvent.ACTION_FIRST, CreateFromBuilderController.CREATE_FROM_BUILDER_COMMAND);
			controller.actionPerformed(event);
		}

    }
    
    @Override
    public void windowClosing(Controller controller) {
    	controller.actionPerformed(new ActionEvent(this, 0, QuitController.COMMAND));
    }
    
    @Override
    protected Image getIcon() {
    	return Icons.FRAME_ICON.getImage();
    }
    
    @Override
    protected JMenuBar createMenuBar(Controller controller) {
    	JMenuBar menuBar = new JMenuBar();
    	JMenu fileMenu = new JMenu("File");
    	fileMenu.setMnemonic(KeyEvent.VK_F);

		JMenuItem createItem = new JMenuItem("Create...");
		createItem.setMnemonic(KeyEvent.VK_C);
		createItem.setIcon(Icons.CREATE_FROM_BUILDER);
		createItem.setActionCommand(CreateFromBuilderController.CREATE_FROM_BUILDER_COMMAND);
		createItem.addActionListener(controller);
    	
    	JMenuItem openItem = new JMenuItem("Open...");
    	openItem.setMnemonic(KeyEvent.VK_O);
    	openItem.setIcon(SwingUtils.createImageIcon("/images/page_go.png"));
    	openItem.setActionCommand(LoadController.LOAD_COMMAND);
		openItem.setAccelerator(KeyStroke.getKeyStroke("F3"));
    	openItem.addActionListener(controller);
    	
    	final JMenuItem saveItem = new JMenuItem("Save");
    	saveItem.setActionCommand(SaveController.SAVE_COMMAND);
    	saveItem.setMnemonic(KeyEvent.VK_S);
    	saveItem.addActionListener(controller);
		saveItem.setAccelerator(KeyStroke.getKeyStroke("F2"));
    	saveItem.setIcon(SwingUtils.createImageIcon("/images/page_save.png"));
    	
    	final JMenuItem saveAsItem = new JMenuItem("Save As...");
    	saveAsItem.setActionCommand(SaveController.SAVE_AS_COMMAND);
    	saveAsItem.addActionListener(controller);

		final JMenuItem closeItem = new JMenuItem("Close");
		closeItem.setActionCommand(CloseController.CLOSE_COMMAND);
		closeItem.addActionListener(controller);
    	
        final JMenuItem saveScreenshotItem = new JMenuItem("Save Screenshot");
        saveScreenshotItem.setActionCommand(TakeScreenshotController.COMMAND);
        saveScreenshotItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK));
        saveScreenshotItem.addActionListener(controller);

    	JMenuItem preferencesItem = new JMenuItem("Preferences...");
        preferencesItem.setActionCommand(PreferencesController.COMMAND);
        preferencesItem.addActionListener(controller);
    	
    	JMenuItem exitItem = new JMenuItem("Exit...");
    	exitItem.setActionCommand(QuitController.COMMAND);
    	exitItem.setIcon(SwingUtils.createImageIcon("/images/door_out.png"));
    	exitItem.setMnemonic(KeyEvent.VK_X);
    	exitItem.addActionListener(controller);

		fileMenu.add(createItem);
    	fileMenu.add(openItem);
    	fileMenu.add(saveItem);
    	fileMenu.add(saveAsItem);
		fileMenu.add(closeItem);
		fileMenu.addSeparator();
        fileMenu.add(saveScreenshotItem);
    	fileMenu.addSeparator();
    	fileMenu.add(preferencesItem);
    	fileMenu.addSeparator();
    	fileMenu.add(exitItem);
    	
    	final JMenu projectMenu = new JMenu("Project");
    	JMenuItem timeItem = new JMenuItem("Time...");
    	timeItem.setIcon(Icons.TIME);
        timeItem.addActionListener(controller);
		timeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_DOWN_MASK));
        timeItem.setActionCommand(TimeController.TIME_DIALOG);

    	JMenuItem findItem = new JMenuItem("Find...");
    	JMenuItem newSpacecraftItem = new JMenuItem("New Spacecraft...");
        newSpacecraftItem.setIcon(Icons.ADD_SPACECRAFT);
        newSpacecraftItem.setActionCommand(SpacecraftController.NEW_SPACECRAFT);
        newSpacecraftItem.addActionListener(controller);
        
        JMenuItem deleteSpacecraftItem = new JMenuItem("Delete Spacecraft...");
        deleteSpacecraftItem.setIcon(Icons.DELETE_SPACECRAFT);
        deleteSpacecraftItem.setActionCommand(SpacecraftController.DELETE_SPACECRAFT);
        deleteSpacecraftItem.addActionListener(controller);
        
    	JMenuItem newPointItem = new JMenuItem("New Point...");
    	newPointItem.setActionCommand(UserPointController.NEW_USER_POINT);
    	newPointItem.addActionListener(controller);
    	
    	JMenuItem interplanetaryFlightItem = new JMenuItem("Interplanetary Flight...");
    	interplanetaryFlightItem.setActionCommand(InterplanetaryFlightController.INTERPLANETARY_FLIGHT);
    	interplanetaryFlightItem.addActionListener(controller);
    	
    	projectMenu.add(timeItem);
    	projectMenu.add(findItem);
        projectMenu.addSeparator();
    	projectMenu.add(newSpacecraftItem);
        projectMenu.add(deleteSpacecraftItem);
    	projectMenu.add(newPointItem);
    	projectMenu.add(interplanetaryFlightItem);
    	
    	JMenu helpMenu = new JMenu("Help");
    	helpMenu.setMnemonic(KeyEvent.VK_H);
    	JMenuItem documentationItem = new JMenuItem("Documentation...");
    	JMenuItem aboutItem = new JMenuItem("About...");
    	helpMenu.add(documentationItem);
		documentationItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
		documentationItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String url = "https://github.com/momega/spacesimulator/wiki/User-Documentation";
				SwingUtils.openUrl(url);
			}
		});
    	helpMenu.add(aboutItem);
    	
    	menuBar.add(fileMenu);
    	menuBar.add(projectMenu);
    	menuBar.add(helpMenu);
    	
    	logger.info("menubar created");

		RendererModel.getInstance().addPropertyChangeListener(RendererModel.MODEL_READY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				boolean modelReady = (boolean) evt.getNewValue();
				saveAsItem.setEnabled(modelReady);
				saveItem.setEnabled(modelReady);
				projectMenu.setEnabled(modelReady);
				saveScreenshotItem.setEnabled(modelReady);
				closeItem.setEnabled(modelReady);
			}
		});
		RendererModel.getInstance().initPropertyChange(RendererModel.MODEL_READY, RendererModel.getInstance().isModelReady());

    	return menuBar;
    }
    
    @Override
    protected void createStatusBar(final Controller controller, JFrame frame) {
    	JStatusBar statusPanel = new JStatusBar();
        final JLabel statusLabel = new JLabel("The application has started.");
        statusPanel.setLeftComponent(statusLabel);
 
        final JLabel timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        statusPanel.addRightComponent(timeLabel);
        timeLabel.setIcon(Icons.TIME);
 
        final JLabel warpLabel = new JLabel();
        warpLabel.setHorizontalAlignment(JLabel.CENTER);
        statusPanel.addRightComponent(warpLabel);

		final JLabel fpsLabel = new JLabel();
		fpsLabel.setHorizontalAlignment(JLabel.CENTER);
		statusPanel.addRightComponent(fpsLabel);
        
    	RendererModel.getInstance().addModelChangeListener(new ModelChangeListener() {
			@Override
			public void modelChanged(ModelChangeEvent event) {
				if (event instanceof StatusBarEvent) {
					StatusBarEvent e = (StatusBarEvent) event;
					statusLabel.setText(e.getMessage());
				} else {
					Model m = event.getModel();
					timeLabel.setText(TimeUtils.timeAsString(m.getTime()));
				}
			}
		});
    	
    	HistoryPointService historyPointService = Application.getInstance().getService(HistoryPointService.class);
    	final HistoryPointListRenderer historyPointListRenderer = new HistoryPointListRenderer();
    	historyPointService.addHistoryPointListener(new HistoryPointListener() {
			@Override
			public void historyPointCreated(HistoryPoint historyPoint) {
				String message = historyPointListRenderer.getText(historyPoint);
				statusLabel.setText(message);
				ImageIcon icon = historyPointListRenderer.getIcon(historyPoint);
				statusLabel.setIcon(icon);
			}
		});
    	
    	RendererModel.getInstance().addPropertyChangeListener(RendererModel.WARP_FACTOR, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				double warpFactor = (double) evt.getNewValue();
				String w = String.format("%3.5f", warpFactor);
				warpLabel.setText(w);
			}
		});
    	RendererModel.getInstance().initPropertyChange(RendererModel.WARP_FACTOR, RendererModel.getInstance().getWarpFactor());
    	
    	timeLabel.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			if (e.getClickCount()>1) {
    				ActionEvent event = new ActionEvent(timeLabel, e.getID(), TimeController.TIME_DIALOG);
    				controller.actionPerformed(event);
    			}
  	  		}
		});

		RendererModel.getInstance().addPropertyChangeListener(RendererModel.FPS, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String w = String.format("%4.2f FPS", (float)evt.getNewValue());
				fpsLabel.setText(w);
			}
		});
    	
    	frame.add(statusPanel, BorderLayout.SOUTH);
    }
    
    @Override
    protected JToolBar createToolBar(Controller controller) {
    	JToolBar toolBar = new JToolBar();
    	toolBar.setRollover(true);
    	
    	JButton warpDownButton = new JButton();
    	warpDownButton.setIcon(SwingUtils.createImageIcon("/images/control_rewind.png"));
    	warpDownButton.setActionCommand(TimeController.WARP_SLOWER);
    	warpDownButton.addActionListener(controller);

    	JToggleButton startStopButton = new JToggleButton();
    	startStopButton.setIcon(SwingUtils.createImageIcon("/images/control_pause.png"));
    	startStopButton.setSelectedIcon(SwingUtils.createImageIcon("/images/control_play.png"));
    	startStopButton.setActionCommand(TimeController.WARP_STOP_OR_START);
    	startStopButton.addActionListener(controller);

    	JButton warpUp = new JButton();
    	warpUp.setIcon(SwingUtils.createImageIcon("/images/control_fastforward.png"));
    	warpUp.setActionCommand(TimeController.WARP_FASTER);
    	warpUp.addActionListener(controller);
    	
    	JComboBox<PositionProvider> movingObjectsBox = new JComboBox<>();
    	movingObjectsBox.setModel(RendererModel.getInstance().getMovingObjectsModel());
    	movingObjectsBox.setRenderer(new MovingObjectListRenderer());
    	movingObjectsBox.setMaximumSize(new Dimension(300, 100));
    	movingObjectsBox.setActionCommand(TargetController.SELECT_POSITION_PROVIDER);
    	movingObjectsBox.addActionListener(controller);
    	
    	JToggleButton spacecraftButton = new JToggleButton();
    	spacecraftButton.setSelected(true);
    	spacecraftButton.setIcon(Icons.SPACECRAFT);
    	spacecraftButton.setToolTipText("Activate the spacecrafts");
    	spacecraftButton.setActionCommand(ToolbarController.SPACECRAFT_TOGGLE_COMMAND);
    	spacecraftButton.addActionListener(controller);

    	JToggleButton celestialButton = new JToggleButton();
    	celestialButton.setSelected(true);
    	celestialButton.setIcon(Icons.CELESTIAL);
    	celestialButton.setToolTipText("Activate the celestial bodies");
    	celestialButton.setActionCommand(ToolbarController.CELESTIAL_TOGGLE_COMMAND);
    	celestialButton.addActionListener(controller);
    	
    	JToggleButton pointButton = new JToggleButton();
    	pointButton.setSelected(true);
    	pointButton.setIcon(Icons.APSIS_POINT);
    	pointButton.setToolTipText("Activate the points");
    	pointButton.setActionCommand(ToolbarController.POINT_TOGGLE_COMMAND);
    	pointButton.addActionListener(controller);

    	toolBar.add(warpDownButton);
    	toolBar.add(startStopButton);
    	toolBar.add(warpUp);
    	toolBar.addSeparator();
    	toolBar.add(spacecraftButton);
    	toolBar.add(celestialButton);
    	toolBar.add(pointButton);
    	toolBar.add(movingObjectsBox);
    	
    	JButton detailButton = new JButton();
    	detailButton.setIcon(SwingUtils.createImageIcon("/images/magnifier.png"));
    	detailButton.setActionCommand(TargetController.DETAIL_POSITION_PROVIDER);
    	detailButton.addActionListener(controller);
    	
    	toolBar.add(detailButton);
    	
    	logger.info("toolbar created");
    	
    	return toolBar;
    }

}
