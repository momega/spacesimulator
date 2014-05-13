package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.model.FreeCamera;
import com.momega.spacesimulator.opengl.AbstractController;

import java.awt.event.KeyEvent;

/**
 * Created by martin on 5/8/14.
 */
public class FreeCameraController extends AbstractController {

    private final FreeCamera camera;

    public FreeCameraController(FreeCamera camera) {
        this.camera = camera;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W:
                camera.moveByVelocity(+1);
                break;

            case KeyEvent.VK_S:
                camera.moveByVelocity(-1);
                break;

            case KeyEvent.VK_OPEN_BRACKET :
                camera.changeVelocity(0.1);
                break;

            case KeyEvent.VK_CLOSE_BRACKET:
                camera.changeVelocity(10);
                break;
        }
    }

}
