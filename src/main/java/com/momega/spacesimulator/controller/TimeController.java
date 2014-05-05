package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.model.Time;
import com.momega.spacesimulator.opengl.AbstractController;

import java.awt.event.KeyEvent;

/**
 * Created by martin on 5/5/14.
 */
public class TimeController extends AbstractController {

    private final Time time;

    public TimeController(Time time) {
        this.time = time;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_COMMA :
                time.changeWarpFactor(0.1);
                break;

            case KeyEvent.VK_PERIOD:
                time.changeWarpFactor(10);
                break;
        }
    }

}
