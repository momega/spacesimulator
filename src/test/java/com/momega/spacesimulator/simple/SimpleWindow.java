package com.momega.spacesimulator.simple;

import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import com.momega.spacesimulator.controller.Controller;
import com.momega.spacesimulator.controller.EventBusController;
import com.momega.spacesimulator.controller.QuitController;
import com.momega.spacesimulator.opengl.DefaultWindow;

/**
 * Created by martin on 7/1/14.
 */
public class SimpleWindow extends DefaultWindow {

    public SimpleWindow(String title) {
        super(title);
    }

    public static void main(String[] args) {
        SimpleWindow window = new SimpleWindow("Test Window");
        EventBusController controller = EventBusController.getInstance();

        controller.addController(new QuitController(window));
        SimpleGLRenderer r = new SimpleGLRenderer();
        window.openWindow(r, controller);
    }

	@Override
	protected JMenuBar createMenuBar(Controller controller) {
		return null;
	}
	
	@Override
	protected JToolBar createToolBar(Controller controller) {
		return null;
	}
}
