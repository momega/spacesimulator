package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.PhysicalBody;
import com.momega.spacesimulator.model.UserOrbitalPoint;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.utils.VectorUtils;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import javax.media.opengl.GL2;

import java.awt.geom.Rectangle2D;

/**
 * Created by martin on 10/29/14.
 */
public class TestingTextRenderer extends AbstractText3dRenderer {

    private final PhysicalBody spacecraft;

    public TestingTextRenderer(PhysicalBody spacecraft) {
        this.spacecraft = spacecraft;
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        Camera camera = ModelHolder.getModel().getCamera();
        for(UserOrbitalPoint orbitalPoint : spacecraft.getUserOrbitalPoints()) {
            gl.glPushMatrix();

            Vector3d position = orbitalPoint.getPosition();
            String str = orbitalPoint.getName();
            Rectangle2D rect = textRenderer.getBounds(str);
            ScreenCoordinates screenCoordinates = GLUtils.getProjectionCoordinates(gl, position, camera);
            screenCoordinates = screenCoordinates.moveBy(-rect.getWidth()/2, -rect.getHeight());

            position = GLUtils.getModelCoordinates(gl, screenCoordinates);

            GLUtils.translate(gl, position);
            Vector3d n = camera.getPosition().subtract(orbitalPoint.getPosition()).normalize();
            Vector3d v = camera.getOppositeOrientation().getV().normalize();

            Rotation r = new Rotation(new Vector3D(0,0,1), new Vector3D(0,1,0), VectorUtils.toVector3D(n), VectorUtils.toVector3D(v));
            double[] angles = r.getAngles(RotationOrder.ZXZ);
            GLUtils.rotateZXZ(gl, angles);

            setColor(new double[]{1, 1, 1});
            drawString(str, Vector3d.ZERO);

            gl.glPopMatrix();
        }
    }



}
