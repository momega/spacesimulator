package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.UserOrbitalPoint;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.renderer.ViewCoordinates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 5/8/14.
 */
public class CameraController extends AbstractCameraConroller {

    private static final Logger logger = LoggerFactory.getLogger(CameraController.class);

    private double height;

    private UserOrbitalPoint selectedUserOrbitalPoint = null;

    public CameraController() {
        super();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if ((e.getModifiers() & InputEvent.BUTTON1_MASK)>0) {
            Point position = GLUtils.getPosition(e);
            final java.util.List<ViewCoordinates> viewCoordinatesList = RendererModel.getInstance().findViewCoordinates(position);
            List<UserOrbitalPoint> points = new ArrayList<>();
            for (ViewCoordinates viewCoordinates : viewCoordinatesList) {
                if (viewCoordinates.getObject() instanceof UserOrbitalPoint) {
                    points.add((UserOrbitalPoint) viewCoordinates.getObject());
                }
            }

            if (selectedUserOrbitalPoint == null && points.size() == 1) {
                selectedUserOrbitalPoint = points.get(0);
                logger.info("selected user orbital point = {}", selectedUserOrbitalPoint);
                RendererModel.getInstance().setSelectedUserOrbitalPoint(selectedUserOrbitalPoint);
                RendererModel.getInstance().setDraggedPoint(position);
                return;
            }

            if (selectedUserOrbitalPoint != null) {
                logger.info("dragging = {}", position);
                RendererModel.getInstance().setDraggedPoint(position);
                return;
            }
        }

        super.mouseDragged(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        selectedUserOrbitalPoint = null;
        RendererModel.getInstance().setDraggedPoint(null);
        RendererModel.getInstance().setSelectedUserOrbitalPoint(null);
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
    
    @Override
    public Camera getCamera() {
    	return ModelHolder.getModel().getCamera();
    }

}
