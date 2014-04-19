package com.momega.spacesimulator.opengl;

import com.momega.spacesimulator.model.Camera;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by martin on 4/19/14.
 */
public class CameraController extends AbstractController {

    private Camera camera;
    private float velocity;
    private static Point mouseLast;

    public CameraController(Camera camera, float velocity) {
        this.camera = camera;
        this.velocity = velocity;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W: // quit
                camera.move(velocity);
                break;

            case KeyEvent.VK_Q: // quit
                camera.twist(+0.5f);
                break;

            case KeyEvent.VK_E: // quit
                camera.twist(-0.5f);
                break;

            case KeyEvent.VK_S:
                camera.move(-velocity);
                break;

            case KeyEvent.VK_O: // quit
                camera.lookLeft(+0.5f);
                break;

            case KeyEvent.VK_P:
                camera.lookLeft(-0.5f);
                break;

            case KeyEvent.VK_H:
                camera.lookUp(+0.5f);
                break;

            case KeyEvent.VK_N:
                camera.lookUp(-0.5f);
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

        final float MOUSE_SPEED_MODIFIER = 0.25f;
        float horizAngle = delta.x*MOUSE_SPEED_MODIFIER, vertAngle = delta.y*MOUSE_SPEED_MODIFIER;

        // Turn horizontally by rotating about the standard up vector (0,0,1).
        camera.lookLeft(-horizAngle);

        // Then look up or down by rotating about u. Note that which way we rotate
        // depends entirely on whether the user wanted the y axis of the mouse
        // inverted or not.
        camera.lookUp(vertAngle);

        //canvas.display();

        mouseLast = new Point(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //canvas.requestFocusInWindow();
    }

}
