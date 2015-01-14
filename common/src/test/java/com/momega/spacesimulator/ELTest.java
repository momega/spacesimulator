package com.momega.spacesimulator;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.Orientation;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.Vector3d;

/**
 * Created by martin on 8/11/14.
 */
public class ELTest {

    @Test
    public void elTest() throws NoSuchMethodException {
        Planet namedObject = new Planet();
        namedObject.setCartesianState(new CartesianState());
        namedObject.getCartesianState().setPosition(new Vector3d(1,2,3));
        namedObject.setOrientation(Orientation.createUnit());

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setVariable("obj", namedObject);
        evaluationContext.registerFunction("toDegrees", Math.class.getDeclaredMethod("toDegrees", double.class));

        // 1
        String exp = "#obj.cartesianState.position.x";
        Expression e = parser.parseExpression(exp);
        String textValue = e.getValue(evaluationContext, String.class);
        Assert.assertEquals("1.0", textValue);

        // 2
        exp = "#toDegrees(#obj.orientation.v.toSphericalCoordinates().phi)";
        e = parser.parseExpression(exp);
        textValue = e.getValue(evaluationContext, String.class);
        Assert.assertEquals("0.0", textValue);
    }
}
