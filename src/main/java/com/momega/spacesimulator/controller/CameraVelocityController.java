package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.opengl.AbstractController;

import java.awt.event.KeyEvent;

/**
 * The controller changes the velocity of the camera
 * Created by martin on 4/26/14.
 */
public class CameraVelocityController extends AbstractController {

    private final Camera camera;

    public CameraVelocityController(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_OPEN_BRACKET :
                camera.changeVelocity(0.1);
                break;

            case KeyEvent.VK_CLOSE_BRACKET:
                camera.changeVelocity(10);
                break;
        }
    }
}
