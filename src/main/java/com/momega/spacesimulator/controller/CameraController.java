package com.momega.spacesimulator.controller;

import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.renderer.ViewCoordinates;

/**
 * Created by martin on 5/8/14.
 */
public class CameraController extends SimpleCameraController {

    private static final Logger logger = LoggerFactory.getLogger(CameraController.class);

    private double height;

    public CameraController(Camera camera) {
        super(camera);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W:
                changeDistance(0.5);
                break;

            case KeyEvent.VK_S:
                changeDistance(2);
                break;
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        logger.info("width = {}, height = {}", e.getComponent().getWidth(), e.getComponent().getHeight());
        this.height = e.getComponent().getHeight();
    }

    public void changeDistance(double factor) {
        if (getCamera().getTargetObject() instanceof PositionProvider) {
            ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(getCamera().getTargetObject());
            logger.info("view radius = {}", viewCoordinates.getRadius());
            if (viewCoordinates.getRadius() * 2 > this.height && factor<1) {
                return;
            }
        }
        super.changeDistance(factor);
    }

}
