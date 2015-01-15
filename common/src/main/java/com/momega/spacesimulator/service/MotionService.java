package com.momega.spacesimulator.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.RunStep;

/**
 * The motion service is responsible for computing new position of moving objects such as planets and satellites.
 * Generally it computes new time and then call two services {@link com.momega.spacesimulator.service.TrajectoryService}
 * and {@link com.momega.spacesimulator.service.RotationService} to compute new position and rotation of all registered
 * moving objects
 * Created by martin on 5/25/14.
 */
@Component
public class MotionService {

    private static final Logger logger = LoggerFactory.getLogger(MotionService.class);
    
    @Autowired
    private ModelService modelService;
    
    @Autowired
    private List<Propagator> propagators = new ArrayList<>();

    /**
     * Moves all the moving object of the model
     * @param step the computation step
     */
    public void move(RunStep step) {
        logger.debug("time={}", step.getNewTimestamp().getValue());
        if (step.getDt() > 0) {
            for (MovingObject mo : modelService.findAllMovingObjects()) {
                move(mo, step);
            }
        }
    }
    
    /**
     * Computes the position of and object in the time newTimestamp. The set new position, velocity and orientation
     * @param movingObject the moving objects
     * @param step the computation step 
     */
    public void move(MovingObject movingObject, RunStep step) {
        Assert.notNull(movingObject);
        for (Propagator propagator : propagators) {
            if (propagator.supports(movingObject)) {
                propagator.computePosition(movingObject, step);
            }
        }
    }    

}
