package com.momega.spacesimulator.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 5/6/14.
 */
public abstract class AbstractModel {

    private static final Logger logger = LoggerFactory.getLogger(AbstractModel.class);

    protected final Time time = new Time(2456760d, 1d);
    //private final Camera camera = new Camera(new Vector3d(0, -147800d, 0), new Vector3d(1, 0, 0), new Vector3d(0, 0, 1), 10);
    //protected final Camera camera = new Camera(new Vector3d(0, 0, 350000d * 5), new Vector3d(0, 0, -1), new Vector3d(0, 1, 0), 10);

    protected final Camera camera = new Camera(new Vector3d(0, 0, 350d * 5), new Vector3d(0, 0, -1), new Vector3d(0, 1, 0), 1);

    protected final List<DynamicalPoint> dynamicalPoints = new ArrayList<>();
    private final List<Planet> planets = new ArrayList<>();

    /**
     * Initialize model
     */
    public final void init() {
        initDynamicalPoints();
        logger.info("model initialized");
    }

    protected void addDynamicalPoint(DynamicalPoint dp) {
        getDynamicalPoints().add(dp);
        if (dp instanceof Planet) {
            planets.add((Planet) dp);
        }
    }

    /**
     * Next step of the iteration
     */
    public void next() {
        for(DynamicalPoint dp : getDynamicalPoints()) {
            dp.move(getTime());
            if (dp instanceof Planet) {
                ((Planet)dp).rotate(getTime());
            }
        }
        getTime().next();
    }

    /**
     * Creates all dynamical points
     */
    protected abstract void initDynamicalPoints();

    public List<DynamicalPoint> getDynamicalPoints() {
        return dynamicalPoints;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public Camera getCamera() {
        return camera;
    }

    public Time getTime() {
        return time;
    }
}
