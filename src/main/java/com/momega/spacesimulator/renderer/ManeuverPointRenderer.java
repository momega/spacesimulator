package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.ManeuverPoint;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.service.ManeuverService;
import com.momega.spacesimulator.utils.TimeUtils;
import org.springframework.util.CollectionUtils;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.util.List;

/**
 * Created by martin on 10/8/14.
 */
public class ManeuverPointRenderer extends AbstractOrbitalPositionProviderRenderer {

    private final Spacecraft spacecraft;
    private final ManeuverService maneuverService;

    public ManeuverPointRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
        maneuverService = Application.getInstance().getService(ManeuverService.class);
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        List<ManeuverPoint> maneuverPoints = maneuverService.findActiveOrNextPoints(spacecraft, ModelHolder.getModel().getTime());
        for(ManeuverPoint maneuverPoint : maneuverPoints) {
            renderPositionProvider(maneuverPoint);
        }
    }

    @Override
    protected void drawObjects(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        List<ManeuverPoint> maneuverPoints = maneuverService.findActiveOrNextPoints(spacecraft, ModelHolder.getModel().getTime());
        for(ManeuverPoint maneuverPoint : maneuverPoints) {
            drawPositionProvider(gl, maneuverPoint, new double[]{0.0, 1.0, 0.0});
        }
    }
}
