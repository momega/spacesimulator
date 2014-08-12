package com.momega.spacesimulator;

import com.momega.spacesimulator.model.Orientation;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.utils.MathUtils;
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

        Vector3d v = VectorUtils.fromSphericalCoordinates(1, Math.PI/2 - delta, alpha);
        double[] angles2 = VectorUtils.toSphericalCoordinates(v);
        logger.info("directly dec = {}, ra = {}", 90-Math.toDegrees(angles2[1]), Math.toDegrees(angles2[2]));

        Orientation orientation = MathUtils.rotateByAngles(alpha, delta,false);

        double[] angles = VectorUtils.toSphericalCoordinates(orientation.getV());
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles[1]), Math.toDegrees(angles[2]));

        Assert.assertTrue(VectorUtils.equals(v, orientation.getV(), 0.00000001));
    }

    @Test
    public void earthTest() {
        double alpha = Math.toRadians(0);
        double delta = Math.toRadians(90);

        Vector3d v = VectorUtils.fromSphericalCoordinates(1, Math.PI/2 - delta, alpha);
        double[] angles2 = VectorUtils.toSphericalCoordinates(v);
        logger.info("directly dec = {}, ra = {}", 90-Math.toDegrees(angles2[1]), Math.toDegrees(angles2[2]));

        Orientation orientation = MathUtils.rotateByAngles(alpha, delta, false);

        double[] angles = VectorUtils.toSphericalCoordinates(orientation.getV());
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles[1]), Math.toDegrees(angles[2]));

        Assert.assertTrue(VectorUtils.equals(v, orientation.getV(), 0.00000001));
    }

    @Test
    public void earthEclipticTest() {
        double alpha = Math.toRadians(90.00000);
        double delta = Math.toRadians(66.56071);

        Vector3d v = VectorUtils.fromSphericalCoordinates(1, Math.PI/2 - delta, alpha);
        double[] angles2 = VectorUtils.toSphericalCoordinates(v);
        logger.info("directly dec = {}, ra = {}", 90-Math.toDegrees(angles2[1]), Math.toDegrees(angles2[2]));

        alpha = Math.toRadians(0);
        delta = Math.toRadians(90);

        Orientation orientation = MathUtils.rotateByAngles(alpha, delta, true);

        double[] angles = VectorUtils.toSphericalCoordinates(orientation.getV());
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles[1]), Math.toDegrees(angles[2]));

        Assert.assertTrue(VectorUtils.equals(v, orientation.getV(), 0.000001));
    }

    @Test
    public void marsEclipticTest() {
        double alpha = Math.toRadians(352.90764);
        double delta = Math.toRadians(63.28205);

        Vector3d v = VectorUtils.fromSphericalCoordinates(1, Math.PI/2 - delta, alpha);
        double[] angles2 = VectorUtils.toSphericalCoordinates(v);
        logger.info("directly dec = {}, ra = {}", 90-Math.toDegrees(angles2[1]), Math.toDegrees(angles2[2]));

        alpha = Math.toRadians(317.68143);
        delta = Math.toRadians(52.8865);

        Orientation orientation = MathUtils.rotateByAngles(alpha, delta, true);

        double[] angles = VectorUtils.toSphericalCoordinates(orientation.getV());
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles[1]), Math.toDegrees(angles[2]));

        Assert.assertTrue(VectorUtils.equals(v, orientation.getV(), 0.000001));
    }

}
