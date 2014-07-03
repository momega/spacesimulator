package com.momega.spacesimulator;

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
}
