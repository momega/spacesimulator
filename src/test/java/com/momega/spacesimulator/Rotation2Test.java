package com.momega.spacesimulator;

import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.utils.VectorUtils;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by martin on 10/17/14.
 */
public class Rotation2Test {

    @Test
    public void zxzTest() {
        KeplerianOrbit keplerianOrbit = new KeplerianOrbit();
        keplerianOrbit.setSemimajorAxis(7406832.895913694);
        keplerianOrbit.setEccentricity(0.11085928349395398);
        keplerianOrbit.setArgumentOfPeriapsis(3.632666857853031);
        keplerianOrbit.setInclination(0.40818100468528823);
        keplerianOrbit.setAscendingNode(3.138751509359614);

        Vector3d v = new Vector3d(0.9907027401573953, -0.1253069535327467, 0.05297403176057446);
        //Vector3d tv = VectorUtils.transform(keplerianOrbit, v);
        Vector3d tv = v;
        tv = VectorUtils.rotateAboutAxis(tv, -keplerianOrbit.getAscendingNode(), new Vector3d(0,0,1));
        tv = VectorUtils.rotateAboutAxis(tv, -keplerianOrbit.getInclination(), new Vector3d(1,0,0));
        tv = VectorUtils.rotateAboutAxis(tv, -keplerianOrbit.getArgumentOfPeriapsis(), new Vector3d(0,0,1));

        Assert.assertEquals(0, tv.getZ(), 0.0001);
        Assert.assertEquals(-0.5850400486216911, tv.getY(), 0.0001);
        Assert.assertEquals(0.8110044028910874, tv.getX(), 0.0001);
    }
}
