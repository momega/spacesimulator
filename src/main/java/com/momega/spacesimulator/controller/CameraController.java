package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.opengl.AbstractController;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * The controller handles moving camera.
 * Keys A,S,D,W,O,P,H,N,Q,E are registered for moving with the camera.
 * Also the controller supports mouse moves.
 * Created by martin on 4/19/14.
 */
public class CameraController extends AbstractController {

    private Camera camera;
    private static Point mouseLast;

    public CameraController(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W:
                camera.moveByVelocity(+1);
                break;

            case KeyEvent.VK_Q:
                camera.getOrientation().twist(+0.5);
                break;

            case KeyEvent.VK_E:
                camera.getOrientation().twist(-0.5);
                break;

            case KeyEvent.VK_S:
                camera.moveByVelocity(-1);
                break;

            case KeyEvent.VK_O:
                camera.getOrientation().lookLeft(+0.5);
                break;

            case KeyEvent.VK_P:
                camera.getOrientation().lookLeft(-0.5);
                break;

            case KeyEvent.VK_H:
                camera.getOrientation().lookUp(+0.5);
                break;

            case KeyEvent.VK_N:
                camera.getOrientation().lookUp(-0.5);
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseLast = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(mouseLast == null) {
            mouseLast = new Point(e.getX(), e.getY());
            return;
        }

        Point delta = new Point(e.getX() - mouseLast.x, e.getY() - mouseLast.y);

        final double MOUSE_SPEED_MODIFIER = 0.25f;
        double horizAngle = delta.x*MOUSE_SPEED_MODIFIER, vertAngle = delta.y*MOUSE_SPEED_MODIFIER;

        // Turn horizontally by rotating about the standard up vector (0,0,1).
        camera.getOrientation().lookLeft(-horizAngle);

        // Then look up or down by rotating about u. Note that which way we rotate
        // depends entirely on whether the user wanted the y axis of the mouse
        // inverted or not.
        camera.getOrientation().lookUp(vertAngle);

        //canvas.display();

        mouseLast = new Point(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //canvas.requestFocusInWindow();
    }

}
