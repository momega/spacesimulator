package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.model.AttachedCamera;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.FreeCamera;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 5/15/14.
 */
public class CompositeCameraController extends AbstractCameraController {

    private final com.momega.spacesimulator.model.CompositeCamera compositeCamera;
    private List<AbstractController> cameraControllers = new ArrayList<>();

    public CompositeCameraController(com.momega.spacesimulator.model.CompositeCamera compositeCamera) {
        this.compositeCamera = compositeCamera;
        for(Camera c : compositeCamera.getCameras()) {
            if (c instanceof FreeCamera) {
                cameraControllers.add(new FreeCameraController((FreeCamera) c));
            } else if (c instanceof AttachedCamera) {
                cameraControllers.add(new SatelliteCameraController((AttachedCamera) c));
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int index = compositeCamera.getCurrent();
        AbstractController c = cameraControllers.get(index);
        c.keyPressed(e);

        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_F2:
                compositeCamera.updateCurrent(+1);
                break;

            case KeyEvent.VK_F1:
                compositeCamera.updateCurrent(-1);
                break;
        }
        super.keyPressed(e);
    }

    @Override
    protected Camera getCamera() {
        return compositeCamera.getCurrentCamera();
    }
}
