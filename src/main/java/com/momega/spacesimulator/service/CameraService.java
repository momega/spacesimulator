package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
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
        Vector3d pos = camera.getDynamicalPoint().getPosition().scaleAdd(camera.getDistance(), camera.getOppositeOrientation().getN());
        Orientation orientation = MathUtils.createOrientation(camera.getOppositeOrientation().getN().negate(), camera.getOppositeOrientation().getV());
        logger.debug("New Position = {}", pos.asArray());
        camera.setPosition(pos);
        camera.setOrientation(orientation);
    }
}
