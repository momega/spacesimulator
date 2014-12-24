package com.momega.spacesimulator.controller;

import java.awt.event.*;
import java.util.EventObject;

import com.momega.spacesimulator.renderer.DelayedActionEvent;

/**
 * The default empty implementation of the controller
 * Created by martin on 4/19/14.
 */
public abstract class AbstractController implements Controller {

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // do nothing
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // do nothing
    }

    @Override
    public void componentResized(ComponentEvent e) {
        // do nothing
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // do nothing
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // do nothing
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // do nothing
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	// do nothing
    }
    
    public void delayedActionPeformed(DelayedActionEvent e) {
    	// do nothing
    }
    
    protected void fireDelayedAction(EventObject event, Object... objects) {
		DelayedActionEvent delayed = new DelayedActionEvent(event.getSource(), event, objects);
    	EventBusController.getInstance().fireDelayedEvent(delayed);
    }

}
