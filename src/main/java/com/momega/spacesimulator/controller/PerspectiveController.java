package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.opengl.MainRenderer;

import java.awt.event.KeyEvent;

/**
 * Created by martin on 5/18/14.
 */
public class PerspectiveController extends AbstractController {

    private final MainRenderer renderer;

    public PerspectiveController(MainRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_PAGE_UP:
                renderer.changeZnear(2);
                break;

            case KeyEvent.VK_PAGE_DOWN:
                renderer.changeZnear(0.5);
                break;

        }
    }
}
