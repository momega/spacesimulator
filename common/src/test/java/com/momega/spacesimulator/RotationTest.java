package com.momega.spacesimulator;

import com.momega.spacesimulator.model.Orientation;
import com.momega.spacesimulator.model.SphericalCoordinates;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.utils.VectorUtils;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by martin on 7/18/14.
 */
public class RotationTest {

    private static final Logger logger = LoggerFactory.getLogger(RotationTest.class);

    @Test
    public void marsTest() {
        double alpha = Math.toRadians(317.68143);
        double delta = Math.toRadians(52.88650);

        Vector3d v = new SphericalCoordinates(1, Math.PI/2 - delta, alpha).toVector();
        SphericalCoordinates angles2 = new SphericalCoordinates(v);
        logger.info("directly dec = {}, ra = {}", 90-Math.toDegrees(angles2.getTheta()), Math.toDegrees(angles2.getPhi()));

        Orientation orientation = VectorUtils.createOrientation(alpha, delta, false);

        SphericalCoordinates angles = new SphericalCoordinates(orientation.getV());
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles.getTheta()), Math.toDegrees(angles.getPhi()));

        Assert.assertTrue(VectorUtils.equals(v, orientation.getV(), 0.00000001));
    }

    @Test
    public void marsTest2() {
        double alpha = Math.toRadians(317.68143);
        double delta = Math.toRadians(52.88650);
        Orientation orientation = VectorUtils.createOrientation(alpha, delta, false);

        SphericalCoordinates angles = new SphericalCoordinates(orientation.getV());
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles.getTheta()), Math.toDegrees(angles.getPhi()));

        Orientation o = Orientation.createUnit();
        o.lookLeft(alpha);
        o.lookUp(Math.PI/2 - delta);

        angles = new SphericalCoordinates(o.getV());
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles.getTheta()), Math.toDegrees(angles.getPhi()));

        Assert.assertTrue(VectorUtils.equals(o.getV(), orientation.getV(), 0.00000001));
    }

    @Test
    public void earthTest() {
        double alpha = Math.toRadians(0);
        double delta = Math.toRadians(90);

        Vector3d v = new SphericalCoordinates(1, Math.PI/2 - delta, alpha).toVector();
        SphericalCoordinates angles2 = new SphericalCoordinates(v);
        logger.info("directly dec = {}, ra = {}", 90-Math.toDegrees(angles2.getTheta()), Math.toDegrees(angles2.getPhi()));

        Orientation orientation = VectorUtils.createOrientation(alpha, delta, false);

        SphericalCoordinates angles = new SphericalCoordinates(orientation.getV());
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles.getTheta()), Math.toDegrees(angles.getPhi()));

        Assert.assertTrue(VectorUtils.equals(v, orientation.getV(), 0.00000001));
    }

    @Test
    public void earthEclipticTest() {
        double alpha = Math.toRadians(90.00000);
        double delta = Math.toRadians(66.56071);

        Vector3d v = new SphericalCoordinates(1, Math.PI/2 - delta, alpha).toVector();
        SphericalCoordinates angles2 = new SphericalCoordinates(v);
        logger.info("directly dec = {}, ra = {}", 90-Math.toDegrees(angles2.getTheta()), Math.toDegrees(angles2.getPhi()));

        alpha = Math.toRadians(0);
        delta = Math.toRadians(90);
        Orientation orientation = VectorUtils.createOrientation(alpha, delta, true);

        SphericalCoordinates angles = new SphericalCoordinates(orientation.getV());
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles.getTheta()), Math.toDegrees(angles.getPhi()));

        Assert.assertTrue(VectorUtils.equals(v, orientation.getV(), 0.000001));
    }

    @Test
    public void marsEclipticTest() {
        double alpha = Math.toRadians(352.90764);
        double delta = Math.toRadians(63.28205);

        Vector3d v = new SphericalCoordinates(1, Math.PI/2 - delta, alpha).toVector();
        SphericalCoordinates angles2 = new SphericalCoordinates(v);
        logger.info("directly dec = {}, ra = {}", 90-Math.toDegrees(angles2.getTheta()), Math.toDegrees(angles2.getPhi()));

        alpha = Math.toRadians(317.68143);
        delta = Math.toRadians(52.8865);
        Orientation orientation = VectorUtils.createOrientation(alpha, delta, true);

        SphericalCoordinates angles = new SphericalCoordinates(orientation.getV());
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles.getTheta()), Math.toDegrees(angles.getPhi()));

        Assert.assertTrue(VectorUtils.equals(v, orientation.getV(), 0.000001));
    }

    @Test
    public void marsEclipticTest2() {
        double alpha = Math.toRadians(317.68143);
        double delta = Math.toRadians(52.8865);
        Orientation orientation = VectorUtils.createOrientation(alpha, delta, true);

        SphericalCoordinates angles = new SphericalCoordinates(orientation.getV());
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles.getTheta()), Math.toDegrees(angles.getPhi()));

        Orientation o = Orientation.createUnit();
        o.lookUp(Math.PI / 2 - delta);
        o.lookLeft(alpha);
        o.rotate(new Vector3d(1, 0, 0), -Math.toRadians(23.439291));
        Vector3d v = o.getV();

        angles = new SphericalCoordinates(o.getV());
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles.getTheta()), Math.toDegrees(angles.getPhi()));

        Assert.assertTrue(VectorUtils.equals(v, orientation.getV(), 0.00000001));
    }

}
