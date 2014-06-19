/**
 *
 */
package com.momega.spacesimulator;

import com.momega.spacesimulator.builder.AbstractModelBuilder;
import com.momega.spacesimulator.builder.SolarSystemModelBuilder;
import com.momega.spacesimulator.context.AppConfig;
import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.controller.*;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.controller.EventBusController;
import com.momega.spacesimulator.opengl.MainRenderer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


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

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        AbstractModelBuilder builder = new SolarSystemModelBuilder();

        Application application = new Application(applicationContext);
        Model model = application.init(builder);

        MainRenderer mr = new MainRenderer(model, application);
        controller.addController(new QuitController(window));
        controller.addController(new TargetController(model));
        controller.addController(new CameraController(model.getCamera()));
        controller.addController(new TimeController(model));
        controller.addController(new PerspectiveController(mr));
        window.openWindow(mr, controller);
    }

}
