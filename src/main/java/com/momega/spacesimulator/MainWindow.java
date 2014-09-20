/**
 *
 */
package com.momega.spacesimulator;

import com.momega.spacesimulator.builder.AbstractModelBuilder;
import com.momega.spacesimulator.builder.SimpleSolarSystemModelBuilder;
import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.controller.CameraController;
import com.momega.spacesimulator.controller.EventBusController;
import com.momega.spacesimulator.controller.PerspectiveController;
import com.momega.spacesimulator.controller.QuitController;
import com.momega.spacesimulator.controller.TargetController;
import com.momega.spacesimulator.controller.TimeController;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.opengl.MainGLRenderer;


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

        //AbstractModelBuilder builder = new EarthSystemModelBuilder();
        AbstractModelBuilder builder = new SimpleSolarSystemModelBuilder();
        //AbstractModelBuilder builder = new SolarSystemModelBuilder();
        //AbstractModelBuilder builder = new FullSolarSystemModelBuilder();

        Application application = new Application();
        Model model = application.init(builder, 5910 * 60);

        MainGLRenderer mr = new MainGLRenderer(application);
        controller.addController(new QuitController(window));
        controller.addController(new TargetController());
        controller.addController(new CameraController(model.getCamera()));
        controller.addController(new TimeController());
        controller.addController(new PerspectiveController(mr));
        window.openWindow(mr, controller);
    }

}
