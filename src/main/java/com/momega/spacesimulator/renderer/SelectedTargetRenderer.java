package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.AbstractModel;
import com.momega.spacesimulator.model.DynamicalPoint;

import javax.media.opengl.GL2;

/**
 * Created by martin on 6/7/14.
 */
public class SelectedTargetRenderer extends AbstractDynamicalPointRenderer {

    private final AbstractModel model;

    public SelectedTargetRenderer(AbstractModel model) {
        this.model = model;
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        DynamicalPoint sp = model.getSelectedDynamicalPoint();
        if (sp != null) {
            drawData(sp, model.getCamera(), width - 350, 50);
        }
    }

}
