package com.momega.spacesimulator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.event.KeyEvent;
import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.controller.CameraController;
import com.momega.spacesimulator.controller.Controller;
import com.momega.spacesimulator.controller.EventBusController;
import com.momega.spacesimulator.controller.InterplanetaryFlightController;
import com.momega.spacesimulator.controller.LoadController;
import com.momega.spacesimulator.controller.PerspectiveController;
import com.momega.spacesimulator.controller.PreferencesController;
import com.momega.spacesimulator.controller.QuitController;
import com.momega.spacesimulator.controller.SaveController;
import com.momega.spacesimulator.controller.SpacecraftController;
import com.momega.spacesimulator.controller.TakeScreenshotController;
import com.momega.spacesimulator.controller.TargetController;
import com.momega.spacesimulator.controller.TimeController;
import com.momega.spacesimulator.controller.ToolbarController;
import com.momega.spacesimulator.controller.UserPointController;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.opengl.MainGLRenderer;
import com.momega.spacesimulator.renderer.ModelChangeEvent;
import com.momega.spacesimulator.renderer.ModelChangeListener;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.swing.Icons;
import com.momega.spacesimulator.swing.MovingObjectListRenderer;
import com.momega.spacesimulator.swing.SwingUtils;


/**
 * The main window of the application
 * @author martin
 */
public class MainWindow extends DefaultWindow {

	private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);
	
    public MainWindow(String title) {
        super(title);
    }

    public static void main(String[] args) {
        final MainWindow window = new MainWindow("Space Simulator");
        EventBusController controller = EventBusController.getInstance();

        Application application = Application.getInstance();
        application.init(0);
        
        MainGLRenderer mr = new MainGLRenderer();
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
    	
    	JMenuItem openItem = new JMenuItem("Open...");
    	openItem.setMnemonic(KeyEvent.VK_O);
    	openItem.setIcon(SwingUtils.createImageIcon("/images/page_go.png"));
    	openItem.setActionCommand(LoadController.LOAD_COMMAND);
    	openItem.addActionListener(controller);
    	
    	JMenuItem saveItem = new JMenuItem("Save");
    	saveItem.setActionCommand(SaveController.SAVE_COMMAND);
    	saveItem.setMnemonic(KeyEvent.VK_S);
    	saveItem.addActionListener(controller);
    	saveItem.setIcon(SwingUtils.createImageIcon("/images/page_save.png"));
    	
    	JMenuItem saveAsItem = new JMenuItem("Save As...");
    	saveAsItem.setActionCommand(SaveController.SAVE_AS_COMMAND);
    	saveAsItem.addActionListener(controller);
    	
        JMenuItem saveScreenshotItem = new JMenuItem("Save Screenshot");
        saveScreenshotItem.setActionCommand(TakeScreenshotController.COMMAND);
        saveScreenshotItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_DOWN_MASK));
        saveScreenshotItem.addActionListener(controller);
    	JMenuItem preferencesItem = new JMenuItem("Preferences...");
        preferencesItem.setActionCommand(PreferencesController.COMMAND);
        preferencesItem.addActionListener(controller);
    	
    	JMenuItem exitItem = new JMenuItem("Exit...");
    	exitItem.setActionCommand(QuitController.COMMAND);
    	exitItem.setIcon(SwingUtils.createImageIcon("/images/door_out.png"));
    	exitItem.setMnemonic(KeyEvent.VK_X);
    	exitItem.addActionListener(controller);
    	
    	fileMenu.add(openItem);
    	fileMenu.add(saveItem);
    	fileMenu.add(saveAsItem);
        fileMenu.add(saveScreenshotItem);
    	fileMenu.addSeparator();
    	fileMenu.add(preferencesItem);
    	fileMenu.addSeparator();
    	fileMenu.add(exitItem);
    	
    	JMenu projectMenu = new JMenu("Project");
    	JMenuItem timeItem = new JMenuItem("Time...");
    	timeItem.setIcon(SwingUtils.createImageIcon("/images/time.png"));
        timeItem.addActionListener(controller);
        timeItem.setActionCommand(TimeController.TIME_DIALOG);

    	JMenuItem findItem = new JMenuItem("Find...");
    	JMenuItem newSpacecraftItem = new JMenuItem("New Spacecraft...");
        newSpacecraftItem.setIcon(Icons.ADD_SPACECRAFT);
        newSpacecraftItem.setActionCommand(SpacecraftController.NEW_SPACECRAFT);
        newSpacecraftItem.addActionListener(controller);
        
        JMenuItem deleteSpacecraftItem = new JMenuItem("Delete Spacecraft...");
        deleteSpacecraftItem.setIcon(Icons.DELETE_SPACECRAFT);
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
    	JMenuItem contentsItem = new JMenuItem("Help Contents");
    	JMenuItem keysItem = new JMenuItem("Key Assists");
    	JMenuItem documentationItem = new JMenuItem("Documentation");
    	JMenuItem aboutItem = new JMenuItem("About...");
    	helpMenu.add(contentsItem);
    	helpMenu.add(keysItem);
    	helpMenu.add(documentationItem);
    	helpMenu.addSeparator();
    	helpMenu.add(aboutItem);
    	
    	menuBar.add(fileMenu);
    	menuBar.add(projectMenu);
    	menuBar.add(helpMenu);
    	
    	logger.info("menubar created");
    	
    	return menuBar;
    }
    
    @Override
    protected void createStatusBar(Controller controller, JFrame frame) {
    	JPanel statusPanel = new JPanel();
    	statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	frame.add(statusPanel, BorderLayout.SOUTH);
    	statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
    	statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
    	JLabel statusLabel = new JLabel("");
    	statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
    	statusPanel.add(statusLabel);
    	
//    	RendererModel.getInstance().addModelChangeListener(new ModelChangeListener() {
//			@Override
//			public void modelChanged(ModelChangeEvent event) {
//				
//			}
//		});
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
    	
    	JComboBox<PositionProvider> movingObjectsBox = new JComboBox<PositionProvider>();
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

        JToggleButton historyPointButton = new JToggleButton();
        historyPointButton.setSelected(true);
        historyPointButton.setIcon(Icons.HISTORY_POINT);
        historyPointButton.setToolTipText("Activate the history points");
        historyPointButton.setActionCommand(ToolbarController.HISTORY_POINT_TOGGLE_COMMAND);
        historyPointButton.addActionListener(controller);
    	
    	toolBar.add(warpDownButton);
    	toolBar.add(startStopButton);
    	toolBar.add(warpUp);
    	toolBar.addSeparator();
    	toolBar.add(spacecraftButton);
    	toolBar.add(celestialButton);
    	toolBar.add(pointButton);
        toolBar.add(historyPointButton);
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
