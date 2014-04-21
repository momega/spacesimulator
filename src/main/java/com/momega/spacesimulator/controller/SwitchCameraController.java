package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.Kepler;
import com.momega.spacesimulator.opengl.AbstractController;

import java.awt.event.KeyEvent;

/**
 * Created by martin on 4/21/14.
 */
public class SwitchCameraController extends AbstractController {

    private final Kepler kepler;

    public SwitchCameraController(Kepler kepler) {
        this.kepler = kepler;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_F1: // quit
                kepler.incCameraIndex();
                break;

        }

    }
}
