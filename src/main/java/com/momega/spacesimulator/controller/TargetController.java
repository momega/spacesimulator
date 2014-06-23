package com.momega.spacesimulator.controller;

import javax.media.opengl.awt.GLCanvas;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.renderer.ViewCoordinates;
import com.momega.spacesimulator.renderer.RendererModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GLDrawable;
import java.awt.event.MouseEvent;

/**
 * Created by martin on 6/7/14.
 */
public class TargetController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(CameraController.class);

    private final static int MIN_TARGET_SIZE = 5;

    private final Model model;

    public TargetController(Model model) {
        this.model = model;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        GLCanvas canvas  = (GLCanvas) e.getSource();
        GLDrawable drawable = canvas.getDelegatedDrawable();
        int x = e.getX();
        int y = e.getY();
        if (drawable.isGLOriented()) {
            y = drawable.getHeight() - y;
        }
        selectDynamicalPoint(x, y);
        super.mouseClicked(e);
    }

    public DynamicalPoint selectDynamicalPoint(int x, int y) {
        for(DynamicalPoint dp : model.getDynamicalPoints()) {
            ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(dp);
            if (viewCoordinates!= null && viewCoordinates.isVisible()) {
                if ((Math.abs(x - viewCoordinates.getX())< MIN_TARGET_SIZE) && (Math.abs(y - viewCoordinates.getY())< MIN_TARGET_SIZE)) {
                    model.setSelectedDynamicalPoint(dp);
                    model.getCamera().setDynamicalPoint(dp);
                    logger.info("selected dynamical point changed to {}", dp.getName());
                }
            }
        }
        return model.getSelectedDynamicalPoint();
    }
}
