package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.DynamicalPoint;

import javax.media.opengl.GL2;

/**
 * Created by martin on 6/7/14.
 */
public class SelectedTargetRenderer extends AbstractDynamicalPointRenderer {

    public SelectedTargetRenderer() {
        super();
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        Model model = ModelHolder.getModel();
        DynamicalPoint sp = model.getSelectedDynamicalPoint();
        if (sp != null) {
            drawData(sp, model.getCamera(), width - 350, 50);
        }
    }

}
