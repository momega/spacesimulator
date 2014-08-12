package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 5/27/14.
 */
@Component
public class CameraService {

    private static final Logger logger = LoggerFactory.getLogger(CameraService.class);

    public void updatePosition(Camera camera) {
        if (camera == null) {
            return;
        }
        Vector3d pos = camera.getTargetObject().getPosition().scaleAdd(camera.getDistance(), camera.getOppositeOrientation().getN());
        logger.debug("New Position = {}", pos.asArray());
        camera.setPosition(pos);
    }
}
