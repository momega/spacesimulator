package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.renderer.RendererModel;

import javax.media.opengl.GLAutoDrawable;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by martin on 10/15/14.
 */
public class ClickController extends AbstractController {

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1 && e.getClickCount()>1) {
            final GLAutoDrawable canvas = (GLAutoDrawable) e.getSource();
            Point position = GLUtils.getPosition(canvas, e);
            RendererModel.getInstance().setMouseCoordinates(position);
        }
    }

}
