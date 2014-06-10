package com.momega.spacesimulator.model;

import com.momega.spacesimulator.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by martin on 5/6/14.
 *
 * //TODO: remove this class to the service package
 */
public abstract class AbstractModel {

    public final static double UNIVERSE_RADIUS = 1E19;

    private final static int MIN_TARGET_SIZE = 5;

    private static final Logger logger = LoggerFactory.getLogger(AbstractModel.class);

    protected Timestamp time;
    protected BigDecimal warpFactor;
    protected Camera camera;
    private DynamicalPoint selectedDynamicalPoint;
    protected TrajectoryService trajectoryService;
    private CameraService cameraService;

    protected MotionService motionService;
    protected UniverseService universeService;

    /**
     * Initialize model
     */
    public final void init() {
        initServices();
        initTime();
        initDynamicalPoints();
        initCamera();
        logger.info("model initialized");
    }

    private void initServices() {
        universeService = new UniverseService();

        trajectoryService = new TrajectoryService();
        KeplerianTrajectoryManager ktm = new KeplerianTrajectoryManager();
        NewtonianTrajectoryManager ntm = new NewtonianTrajectoryManager();
        ntm.setUniverseService(universeService);
        StaticTrajectoryManager stm = new StaticTrajectoryManager();
        trajectoryService.setTrajectoryManagers(Arrays.asList(ktm, ntm, stm));

        RotationService rotationService = new RotationService();
        this.motionService = new MotionService();
        this.motionService.setRotationService(rotationService);
        this.motionService.setTrajectoryService(trajectoryService);

        this.cameraService = new CameraService();
    }

    protected abstract void initTime();

    protected abstract void initCamera();



    /**
     * Next step of the model iteration
     */
    public void next() {
        Timestamp t = motionService.move(universeService.getDynamicalPoints(), getTime(),getWarpFactor());
        setTime(t);
        cameraService.updatePosition(camera);
    }

    public double computeZNear(double aratio) {
        Vector3d viewVector = camera.getOrientation().getN();
        double znear = UNIVERSE_RADIUS * 2;

        for(DynamicalPoint dp : universeService.getDynamicalPoints()) {

            // only valid for object with radius
            if (dp.getRadius() <= 0) {
                continue;
            }

            double distance = dp.getPosition().distance(camera.getPosition());
            double distanceFactor = distance / dp.getRadius();
            if (distanceFactor > UNIVERSE_RADIUS * 0.001) {
                continue; // If it's too far to be visible discard it
            }

            // check whether the dynamic point is visible
            Vector3d diffVector = dp.getPosition().subtract(camera.getPosition()).normalize();
            double dot = viewVector.dot(diffVector);
            if (Math.abs(dot) < 0.01) {
                continue;
            }
            double eyeAngle = Math.acos(dot);

            double radiusAngle = Math.atan2(dp.getRadius(), distance);

            double diffAngle = Math.abs(eyeAngle) -  Math.abs(radiusAngle);
            if (diffAngle > Math.toRadians(aratio * 45)) {
                continue;
            }

            double clip = Math.abs(Math.cos(diffAngle) * distance - dp.getRadius()) - 1;
            if (clip < 1) {
                clip = 1;
            }

            if (clip < znear) {
                znear = clip;
            }
        }

        if (znear > UNIVERSE_RADIUS * 0.001) {
            znear = UNIVERSE_RADIUS * 0.001;
        }

        return znear;
    }

    /**
     * Creates all dynamical points
     */
    protected abstract void initDynamicalPoints();



    public DynamicalPoint selectDynamicalPoint(int x, int y) {
        for(DynamicalPoint dp : universeService.getDynamicalPoints()) {
            ViewCoordinates viewCoordinates = dp.getViewCoordinates();
            if (viewCoordinates!= null && viewCoordinates.isVisible()) {
                if ((Math.abs(x - viewCoordinates.getX())< MIN_TARGET_SIZE) && (Math.abs(y - viewCoordinates.getY())< MIN_TARGET_SIZE)) {
                    setSelectedDynamicalPoint(dp);
                    logger.info("selected dynamical point changed to {}", this.selectedDynamicalPoint.getName());
                }
            }
        }
        return this.selectedDynamicalPoint;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setWarpFactor(BigDecimal warpFactor) {
        this.warpFactor = warpFactor;
    }

    public BigDecimal getWarpFactor() {
        return warpFactor;
    }

    public DynamicalPoint getSelectedDynamicalPoint() {
        return selectedDynamicalPoint;
    }

    public void setSelectedDynamicalPoint(DynamicalPoint selectedDynamicalPoint) {
        this.selectedDynamicalPoint = selectedDynamicalPoint;
    }

    public UniverseService getUniverseService() {
        return universeService;
    }
}
