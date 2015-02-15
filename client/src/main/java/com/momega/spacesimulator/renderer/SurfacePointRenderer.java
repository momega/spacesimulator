package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.SurfacePoint;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.swing.Icons;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 1/10/15.
 */
public class SurfacePointRenderer extends AbstractBitmapRenderer {

    private ModelService modelService;

    public SurfacePointRenderer() {
        modelService = Application.getInstance().getService(ModelService.class);
    }

    @Override
    protected ImageIcon getImageIcon() {
        return Icons.CRASH_SITE;
    }

    @Override
    public List<Vector3d> getPositions() {
        List<Vector3d> list = new ArrayList<>();
        Model model = ModelHolder.getModel(); 
        for(CelestialBody body: modelService.findAllCelestialBodies(model)) {
            for(SurfacePoint surfacePoint : body.getSurfacePoints()) {
                list.add(surfacePoint.getPosition());
            }
        }
        return list;
    }

}
