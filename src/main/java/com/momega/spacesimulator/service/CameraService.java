package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;

/**
 * Created by martin on 5/27/14.
 */
public class CameraService {

    public void updatePosition(Camera camera) {
        if (camera instanceof FreeCamera) {
            // do nothing
        } else if (camera instanceof AttachedCamera) {
            AttachedCamera ac = (AttachedCamera) camera;
            double distance = ac.getDistance();
            DynamicalPoint dynamicalPoint = ac.getDynamicalPoint();
            ac.setPosition(Vector3d.scaleAdd(distance, new Vector3d(1d, 0d, 0d), dynamicalPoint.getPosition()));
        } else if (camera instanceof  CompositeCamera) {
            CompositeCamera cc = (CompositeCamera) camera;
            updatePosition(cc.getCurrentCamera());
        }
    }
}
