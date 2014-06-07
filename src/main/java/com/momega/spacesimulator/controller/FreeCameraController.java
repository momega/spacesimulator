package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.model.FreeCamera;

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
                moveByVelocity(+1);
                break;

            case KeyEvent.VK_S:
                moveByVelocity(-1);
                break;

            case KeyEvent.VK_OPEN_BRACKET :
                changeVelocity(0.1);
                break;

            case KeyEvent.VK_CLOSE_BRACKET:
                changeVelocity(10);
                break;
        }
    }


    /**
     * Moves the camera forward or backward with the current velocity
     * @param direction the direction
     *
     */
    public void moveByVelocity(double direction) {
        camera.moveN(camera.getVelocity() * direction);
    }

    public void changeVelocity(double factor) {
        camera.setVelocity(camera.getVelocity() * factor);
    }

}
