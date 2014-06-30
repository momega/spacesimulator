package com.momega.spacesimulator.controller;

import javax.media.opengl.awt.GLCanvas;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.renderer.ViewCoordinates;
import com.momega.spacesimulator.renderer.RendererModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GLDrawable;
import java.awt.event.MouseEvent;

/**
 * Controller which enables selecting the target
 * Created by martin on 6/7/14.
 */
public class TargetController extends AbstractController {

    @Override
    public void mouseClicked(MouseEvent e) {
        GLCanvas canvas  = (GLCanvas) e.getSource();
        GLDrawable drawable = canvas.getDelegatedDrawable();
        int x = e.getX();
        int y = e.getY();
        if (drawable.isGLOriented()) {
            y = drawable.getHeight() - y;
        }
        RendererModel.getInstance().selectDynamicalPoint(x, y);
        super.mouseClicked(e);
    }

}
