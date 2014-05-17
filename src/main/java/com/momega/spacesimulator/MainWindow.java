/**
 *
 */
package com.momega.spacesimulator;

import com.momega.spacesimulator.controller.*;
import com.momega.spacesimulator.model.AbstractModel;
import com.momega.spacesimulator.model.EarthSystemModel;
import com.momega.spacesimulator.model.SatelliteCamera;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.opengl.EventBusController;
import com.momega.spacesimulator.opengl.ModelGLRenderer;
import com.momega.spacesimulator.renderer.ModelRenderer;


/**
 * The main window of the application
 * @author martin
 */
public class MainWindow extends DefaultWindow {

    public MainWindow(String title) {
        super(title);
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow("Space Simulator");
        EventBusController controller = new EventBusController();

        AbstractModel model = new EarthSystemModel();
        model.init();

        ModelRenderer renderer = new ModelRenderer(model);

        ModelGLRenderer glRenderer = new ModelGLRenderer(model, renderer);
        controller.addController(new QuitController(window));
        controller.addController(new CompositeCameraController((com.momega.spacesimulator.model.CompositeCamera) model.getCamera()));
        controller.addController(new TimeController(model.getTime()));
        window.openWindow(glRenderer, controller);
    }

}
