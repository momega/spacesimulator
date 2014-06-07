package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.AbstractModel;
import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.model.Vector3d;

import javax.media.opengl.GL2;

/**
 * Created by martin on 6/7/14.
 */
public class SelectedTargetRenderer extends AbstractTextRenderer {

    private final AbstractModel model;

    public SelectedTargetRenderer(AbstractModel model) {
        this.model = model;
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        DynamicalPoint sp = model.getSelectedDynamicalPoint();
        if (sp != null) {
            drawText(sp.getName(), width - 250, 40);
            drawText("P:" + sp.getPosition().toString(), width - 250, 25);
            double distance = Vector3d.subtract(sp.getPosition(), model.getCamera().getPosition()).length() / 1E9;
            drawText("D:" + String.format("%6.2f", distance), width - 250, 10);
        }
    }

}
