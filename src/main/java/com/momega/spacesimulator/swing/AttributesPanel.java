package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.AbstractOrbitalPoint;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.renderer.ModelChangeEvent;
import com.momega.spacesimulator.utils.KeplerianUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import com.momega.spacesimulator.utils.VectorUtils;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The panel contains the set of the attribute of the {@link com.momega.spacesimulator.model.NamedObject}.
 * The value are computed by the Spring EL expressions.
 * Created by martin on 8/11/14.
 */
public class AttributesPanel extends JPanel implements UpdatablePanel {

	private static final long serialVersionUID = -9096883134657153199L;
	private final Object object;
    protected Map<JComponent, Expression> jFields = new HashMap<>();
    protected List<JLabel> jLabels = new ArrayList<>();

    public AttributesPanel(String[] labels, Object object, String[] fields) {
        this.object = object;
        int numPairs = labels.length;
        GridLayout layout = new GridLayout(numPairs, 2, 5, 5);
        setLayout(layout);
        ExpressionParser parser = new SpelExpressionParser();

        for (int i = 0; i < numPairs; i++) {
            JLabel label = new JLabel(labels[i] + ": ", JLabel.TRAILING);
            add(label);
            JTextField textField = new JTextField(20);
            textField.setEditable(false);
            label.setLabelFor(textField);
            add(textField);

            label.setAlignmentX(Component.RIGHT_ALIGNMENT);

            jLabels.add(label);

            Expression exp = parser.parseExpression(fields[i]);
            jFields.put(textField, exp);
        }
    }

    @Override
    public void updateView(ModelChangeEvent event) {
        EvaluationContext evaluationContext = createContext();
        for(Map.Entry<JComponent, Expression> entry : jFields.entrySet()) {
            if (entry.getValue()!=null) {
                Expression e = entry.getValue();
                String textValue = null;
                try {
                    textValue = e.getValue(evaluationContext, String.class);
                } catch (EvaluationException ee) {
                    // do nothing
                }
                if ( entry.getKey() instanceof JTextComponent) {
                    JTextComponent tc = (JTextComponent) entry.getKey();
                    tc.setText(textValue);
                }
            }
        }
    }
    
    @Override
    public void updateModel() {
    	// do nothing
    }

    protected EvaluationContext createContext() {
        try {
            StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
            evaluationContext.setVariable("obj", object);
            evaluationContext.registerFunction("toSphericalCoordinates", VectorUtils.class.getDeclaredMethod("toSphericalCoordinates", Vector3d.class));
            evaluationContext.registerFunction("toDegrees", Math.class.getDeclaredMethod("toDegrees", double.class));
            evaluationContext.registerFunction("timeAsString", TimeUtils.class.getDeclaredMethod("timeAsString", Timestamp.class));
            evaluationContext.registerFunction("getAltitude", KeplerianUtils.class.getDeclaredMethod("getAltitude", KeplerianElements.class, double.class));
            evaluationContext.registerFunction("getAltitude2", KeplerianUtils.class.getDeclaredMethod("getAltitude", MovingObject.class));
            evaluationContext.registerFunction("getETA", KeplerianUtils.class.getDeclaredMethod("getETA", AbstractOrbitalPoint.class));
            evaluationContext.registerFunction("periodAsString", TimeUtils.class.getDeclaredMethod("periodAsString", double.class));
            return evaluationContext;
        } catch (NoSuchMethodException nsme) {
            throw new IllegalArgumentException(nsme);
        }
    }
}
