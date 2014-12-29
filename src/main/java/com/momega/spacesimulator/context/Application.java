package com.momega.spacesimulator.context;

/**
 * The instance of the class is the singleton holding the main application instances such as spring application
 * context or model walker.
 * Created by martin on 6/18/14.
 */
public class Application extends DefaultApplication {

    private static Application instance = null;

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    private Application() {
        super(AppConfig.class);
    }

}
