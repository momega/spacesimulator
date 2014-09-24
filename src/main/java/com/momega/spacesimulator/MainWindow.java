package com.momega.spacesimulator;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.event.KeyEvent;
import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.controller.CameraController;
import com.momega.spacesimulator.controller.Controller;
import com.momega.spacesimulator.controller.EventBusController;
import com.momega.spacesimulator.controller.PerspectiveController;
import com.momega.spacesimulator.controller.QuitController;
import com.momega.spacesimulator.controller.TargetController;
import com.momega.spacesimulator.controller.TimeController;
import com.momega.spacesimulator.controller.ToolbarController;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.opengl.MainGLRenderer;
import com.momega.spacesimulator.swing.MovingObjectListRenderer;
import com.momega.spacesimulator.swing.SwingUtils;
import com.momega.spacesimulator.swing.WindowModel;


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
        MainWindow window = new MainWindow("Space Simulator");
        EventBusController controller = new EventBusController();

        Application application = new Application();
        
        //Model model = application.init(builder, 293037 - 600);
        //Model model = application.init(builder, 5910 * 60);
        //Model model = application.init(builder, 190 * 60);
        Model model = application.init(0);
        
        MainGLRenderer mr = new MainGLRenderer(application);
        controller.addController(new QuitController(window));
        controller.addController(new TargetController());
        controller.addController(new CameraController(model.getCamera()));
        controller.addController(new TimeController());
        controller.addController(new PerspectiveController(mr));
        controller.addController(new ToolbarController());
        window.openWindow(mr, controller);
    }
    
    @Override
    protected JMenuBar createMenuBar(Controller controller) {
    	JMenuBar menuBar = new JMenuBar();
    	JMenu fileMenu = new JMenu("File");
    	fileMenu.setMnemonic(KeyEvent.VK_F);
    	
    	JMenuItem openItem = new JMenuItem("Open...");
    	openItem.setMnemonic(KeyEvent.VK_O);
    	openItem.setIcon(SwingUtils.createImageIcon("/images/page_go.png"));
    	JMenuItem saveItem = new JMenuItem("Save");
    	saveItem.setMnemonic(KeyEvent.VK_S);
    	saveItem.setIcon(SwingUtils.createImageIcon("/images/page_save.png"));
    	JMenuItem saveAsItem = new JMenuItem("Save As...");
    	JMenuItem preferencesItem = new JMenuItem("Preferences...");
    	
    	JMenuItem exitItem = new JMenuItem("Exit...");
    	exitItem.setActionCommand(QuitController.COMMAND);
    	exitItem.setIcon(SwingUtils.createImageIcon("/images/door_out.png"));
    	exitItem.setMnemonic(KeyEvent.VK_X);
    	exitItem.addActionListener(controller);
    	
    	fileMenu.add(openItem);
    	fileMenu.add(saveItem);
    	fileMenu.add(saveAsItem);
    	fileMenu.addSeparator();
    	fileMenu.add(preferencesItem);
    	fileMenu.addSeparator();
    	fileMenu.add(exitItem);
    	
    	JMenu projectMenu = new JMenu("Project");
    	JMenuItem timeItem = new JMenuItem("Time...");
    	timeItem.setIcon(SwingUtils.createImageIcon("/images/time.png"));
    	JMenuItem findItem = new JMenuItem("Find...");
    	JMenuItem newSpacecraftItem = new JMenuItem("New Spacecraft...");
    	JMenuItem newPointItem = new JMenuItem("New Point...");
    	projectMenu.add(timeItem);
    	projectMenu.add(findItem);
    	projectMenu.add(newSpacecraftItem);
    	projectMenu.add(newPointItem);
    	
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
    	movingObjectsBox.setModel(WindowModel.getInstance().getMovingObjectsModel());
    	movingObjectsBox.setRenderer(new MovingObjectListRenderer());
    	movingObjectsBox.setMaximumSize(new Dimension(300, 100));
    	movingObjectsBox.setActionCommand(TargetController.SELECT_POSITION_PROVIDER);
    	movingObjectsBox.addActionListener(controller);
    	
    	JToggleButton spacecraftButton = new JToggleButton();
    	spacecraftButton.setSelected(true);
    	spacecraftButton.setIcon(SwingUtils.createImageIcon("/images/satellite_16_hot.png"));
    	spacecraftButton.setToolTipText("Activate the spacecrafts");
    	spacecraftButton.setActionCommand(ToolbarController.SPACECRAFT_TOGGLE_COMMAND);
    	spacecraftButton.addActionListener(controller);

    	JToggleButton celesialButton = new JToggleButton();
    	celesialButton.setSelected(true);
    	celesialButton.setIcon(SwingUtils.createImageIcon("/images/100.png"));
    	celesialButton.setToolTipText("Activate the celestial bodies");
    	celesialButton.setActionCommand(ToolbarController.CELESTIAL_TOGGLE_COMMAND);
    	celesialButton.addActionListener(controller);
    	
    	JToggleButton pointButton = new JToggleButton();
    	pointButton.setSelected(true);
    	pointButton.setIcon(SwingUtils.createImageIcon("/images/bullet_blue.png"));
    	pointButton.setToolTipText("Activate the points");
    	pointButton.setActionCommand(ToolbarController.POINT_TOGGLE_COMMAND);
    	pointButton.addActionListener(controller);
    	
    	toolBar.add(warpDownButton);
    	toolBar.add(startStopButton);
    	toolBar.add(warpUp);
    	toolBar.addSeparator();
    	toolBar.add(spacecraftButton);
    	toolBar.add(celesialButton);
    	toolBar.add(pointButton);
    	toolBar.add(movingObjectsBox);
    	
    	JButton detailButton = new JButton();
    	detailButton.setIcon(SwingUtils.createImageIcon("/images/magnifier.png"));
    	detailButton.setActionCommand(TargetController.DELAIL_POSITION_PROVIDER);
    	detailButton.addActionListener(controller);
    	
    	toolBar.add(detailButton);
    	
    	logger.info("toolbar created");
    	
    	return toolBar;
    }

}
