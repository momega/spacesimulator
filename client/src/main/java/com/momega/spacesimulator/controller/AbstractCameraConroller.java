/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.model.Camera;

/**
 * @author martin
 *
 */
public abstract class AbstractCameraConroller extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(AbstractCameraConroller.class);
	
	private Point mouseLast;
	
	public AbstractCameraConroller() {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseLast = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if ((e.getModifiers() & InputEvent.BUTTON1_MASK)>0) {
            return;
        }

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
    
    public void changeDistance(double factor) {
        double newDistance = getCamera().getDistance() * factor;
        logger.debug("new distance of the camera = {}", newDistance);
        getCamera().setDistance(newDistance);
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

    public abstract Camera getCamera();

}
