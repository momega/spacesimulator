/**
 *
 */
package com.momega.spacesimulator;

import java.awt.*;

import com.momega.spacesimulator.controller.CameraController;
import com.momega.spacesimulator.controller.CameraVelocityController;
import com.momega.spacesimulator.controller.QuitController;
import com.momega.spacesimulator.controller.TimeController;
import com.momega.spacesimulator.model.AbstractModel;
import com.momega.spacesimulator.model.EarthSystemModel;
import com.momega.spacesimulator.model.SolarSystemModel;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.opengl.EventBusController;
import com.momega.spacesimulator.renderer.ModelRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
        controller.addController(new QuitController(window));
        controller.addController(new CameraController(model.getCamera()));
        controller.addController(new CameraVelocityController(model.getCamera()));
        controller.addController(new TimeController(model.getTime()));
        window.openWindow(renderer, controller);
    }

}
