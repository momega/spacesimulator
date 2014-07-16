package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.NamedObject;
import com.momega.spacesimulator.model.RotatingObject;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.renderer.ViewCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by martin on 5/8/14.
 */
public class CameraController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(CameraController.class);

    private final Camera camera;

    private double height;

    private Point mouseLast;

    public CameraController(Camera camera) {
        this.camera = camera;
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
        if (camera.getTargetObject() instanceof NamedObject) {
            ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates((NamedObject) camera.getTargetObject());
            logger.info("view radius = {}", viewCoordinates.getRadius());
            if (viewCoordinates.getRadius() * 2 > this.height && factor<1) {
                return;
            }
        }
        double newDistance = camera.getDistance() * factor;
        logger.info("new distance of the camera = {}", newDistance);
        camera.setDistance(newDistance);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseLast = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseLast == null) {
            mouseLast = new Point(e.getX(), e.getY());
            return;
        }

        Point delta = new Point(e.getX() - mouseLast.x, e.getY() - mouseLast.y);

        final double MOUSE_SPEED_MODIFIER = 0.0025f;
        double horizAngle = delta.x*MOUSE_SPEED_MODIFIER, vertAngle = delta.y*MOUSE_SPEED_MODIFIER;

        // Turn horizontally by rotating about the standard up vector (0,0,1).
        getCamera().getOppositeOrientation().lookLeft(-horizAngle);

        // Then look up or down by rotating about u. Note that which way we rotate
        // depends entirely on whether the user wanted the y axis of the mouse
        // inverted or not.
        getCamera().getOppositeOrientation().lookUp(-vertAngle);
        //canvas.display();

        mouseLast = new Point(e.getX(), e.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) {
            changeDistance(0.9);
        } else {
            changeDistance(1.1);
        }
    }

    public Camera getCamera() {
        return camera;
    }

}
