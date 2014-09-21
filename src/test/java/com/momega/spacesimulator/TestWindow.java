package com.momega.spacesimulator;

import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import com.momega.spacesimulator.controller.Controller;
import com.momega.spacesimulator.controller.EventBusController;
import com.momega.spacesimulator.controller.QuitController;
import com.momega.spacesimulator.opengl.DefaultWindow;

/**
 * Created by martin on 7/1/14.
 */
public class TestWindow extends DefaultWindow {

    public TestWindow(String title) {
        super(title);
    }

    public static void main(String[] args) {
        TestWindow window = new TestWindow("Test Window");
        EventBusController controller = new EventBusController();

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
