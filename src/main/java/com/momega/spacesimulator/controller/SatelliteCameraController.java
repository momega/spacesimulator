package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.model.AttachedCamera;

import java.awt.event.KeyEvent;

/**
 * Created by martin on 5/8/14.
 */
public class SatelliteCameraController extends AbstractController {

    private final AttachedCamera camera;

    public SatelliteCameraController(AttachedCamera camera) {
        this.camera = camera;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W:
                camera.changeDistance(0.5);
                break;

            case KeyEvent.VK_S:
                camera.changeDistance(2);
                break;
        }

    }

}
