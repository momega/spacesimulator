package com.momega.spacesimulator.controller;

import javax.media.opengl.awt.GLCanvas;
import com.momega.spacesimulator.model.AbstractModel;

import javax.media.opengl.GLDrawable;
import java.awt.event.MouseEvent;

/**
 * Created by martin on 6/7/14.
 */
public class TargetController extends AbstractController {

    private final AbstractModel model;

    public TargetController(AbstractModel model) {
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
        model.selectDynamicalPoint(x, y);
        super.mouseClicked(e);
    }
}
