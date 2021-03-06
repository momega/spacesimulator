package com.momega.spacesimulator.renderer;

import java.util.List;

import javax.media.opengl.GL2;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.model.ManeuverPoint;
import com.momega.spacesimulator.model.OrbitIntersection;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.service.ManeuverService;
import com.momega.spacesimulator.service.TargetService;

/**
 * Created by martin on 9/6/14.
 */
public class SpacecraftOrbitPointsRenderer extends AbstractOrbitalPositionProviderRenderer {

    private final Spacecraft spacecraft;
    private final ManeuverService maneuverService;
    private final TargetService targetService;

    public SpacecraftOrbitPointsRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
        maneuverService = Application.getInstance().getService(ManeuverService.class);
        targetService = Application.getInstance().getService(TargetService.class);
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        for(OrbitIntersection intersection : targetService.getOrbitIntersections(spacecraft)) {
        	renderPositionProvider(gl, intersection);
        }
        List<ManeuverPoint> maneuverPoints = maneuverService.findActiveOrNextPoints(spacecraft, spacecraft.getTimestamp());
        for(ManeuverPoint maneuverPoint : maneuverPoints) {
            renderPositionProvider(gl, maneuverPoint);
        }
        if (spacecraft.getExitSoiOrbitalPoint()!=null) {
        	renderPositionProvider(gl, spacecraft.getExitSoiOrbitalPoint());
            if (spacecraft.getExitSoiOrbitalPoint().getClosestPoint()!=null) {
            	renderPositionProvider(gl, spacecraft.getExitSoiOrbitalPoint().getClosestPoint());
            }
        }
    }

}
