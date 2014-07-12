package com.momega.spacesimulator.controller;

import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of the controller that passes all method calls to the registered specific/detail controller.
 *
 * Created by martin on 4/19/14.
 */
public class EventBusController extends AbstractController {

    private final List<Controller> controllers = new ArrayList<>();

    @Override
    public void keyTyped(KeyEvent e) {
        for(Controller controller : controllers) {
            controller.keyTyped(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        for(Controller controller : controllers) {
            controller.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for(Controller controller : controllers) {
            controller.keyReleased(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for(Controller controller : controllers) {
            controller.mouseClicked(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(Controller controller : controllers) {
            controller.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for(Controller controller : controllers) {
            controller.mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        for(Controller controller : controllers) {
            controller.mouseEntered(e);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        for(Controller controller : controllers) {
            controller.mouseExited(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(Controller controller : controllers) {
            controller.mouseMoved(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        for(Controller controller : controllers) {
            controller.mouseDragged(e);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        for(Controller controller : controllers) {
            controller.mouseWheelMoved(e);
        }
    }

    public void componentResized(ComponentEvent e) {
        for(Controller controller : controllers) {
            controller.componentResized(e);
        }
    }

    public void componentMoved(ComponentEvent e) {
        for(Controller controller : controllers) {
            controller.componentMoved(e);
        }
    }

    public void componentShown(ComponentEvent e) {
        for(Controller controller : controllers) {
            controller.componentShown(e);
        }
    }

    public void componentHidden(ComponentEvent e) {
        for(Controller controller : controllers) {
            controller.componentHidden(e);
        }
    }

    public void addController(Controller controller) {
        this.controllers.add(controller);
    }
}
