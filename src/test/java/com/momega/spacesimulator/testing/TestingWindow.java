package com.momega.spacesimulator.testing;

import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import com.momega.spacesimulator.controller.Controller;
import com.momega.spacesimulator.controller.EventBusController;
import com.momega.spacesimulator.controller.QuitController;
import com.momega.spacesimulator.controller.SimpleCameraController;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Orientation;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.renderer.CompositeRenderer;
import com.momega.spacesimulator.renderer.DefaultCameraPositionRenderer;
import com.momega.spacesimulator.utils.VectorUtils;

/**
 * Created by martin on 7/1/14.
 */
public class TestingWindow extends DefaultWindow {

    public TestingWindow(String title) {
        super(title, false);
    }

    public static void main(String[] args) {
        TestingWindow window = new TestingWindow("Test Window");
        EventBusController controller = new EventBusController();
        
        Camera camera = new Camera();
        camera.setPosition(Vector3d.ZERO);
        camera.setDistance(10);
        camera.setOppositeOrientation(Orientation.createUnit());
        
        CompositeRenderer renderer = new CompositeRenderer();
        renderer.addRenderer(new DefaultCameraPositionRenderer(camera));

        controller.addController(new QuitController(window));
        controller.addController(new SimpleCameraController(camera));
        TestingGLRenderer r = new TestingGLRenderer(camera, renderer);
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
