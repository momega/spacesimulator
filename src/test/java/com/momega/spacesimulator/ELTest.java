package com.momega.spacesimulator;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.VectorUtils;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Created by martin on 8/11/14.
 */
public class ELTest {

    @Test
    public void elTest() throws NoSuchMethodException {
        Planet namedObject = new Planet();
        namedObject.setCartesianState(new CartesianState());
        namedObject.getCartesianState().setPosition(new Vector3d(1,2,3));
        namedObject.setOrientation(MathUtils.createOrientation(new Vector3d(1, 0, 0), new Vector3d(1, 0, 0)));

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setVariable("obj", namedObject);
        evaluationContext.registerFunction("toSphericalCoordinates", VectorUtils.class.getDeclaredMethod("toSphericalCoordinates", Vector3d.class));
        evaluationContext.registerFunction("toDegrees", Math.class.getDeclaredMethod("toDegrees", double.class));

        // 1
        String exp = "#obj.cartesianState.position.x";
        Expression e = parser.parseExpression(exp);
        String textValue = e.getValue(evaluationContext, String.class);
        Assert.assertEquals("1.0", textValue);

        // 2
        exp = "#toDegrees(#toSphericalCoordinates(#obj.orientation.v)[1])";
        e = parser.parseExpression(exp);
        textValue = e.getValue(evaluationContext, String.class);
        Assert.assertEquals("90.0", textValue);
    }
}
