package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.NamedObject;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.model.Vector3d;
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
public class AttributesPanel extends JPanel {

    private final NamedObject namedObject;
    protected Map<JComponent, Expression> jFields = new HashMap<>();
    protected List<JLabel> jLabels = new ArrayList<>();

    public AttributesPanel(String[] labels, NamedObject namedObject, String[] fields) {
        this.namedObject = namedObject;
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

    protected void updateValues() {
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

    protected EvaluationContext createContext() {
        try {
            StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
            evaluationContext.setVariable("obj", namedObject);
            evaluationContext.registerFunction("toSphericalCoordinates", VectorUtils.class.getDeclaredMethod("toSphericalCoordinates", Vector3d.class));
            evaluationContext.registerFunction("toDegrees", Math.class.getDeclaredMethod("toDegrees", double.class));
            evaluationContext.registerFunction("timeAsString", TimeUtils.class.getDeclaredMethod("timeAsString", Timestamp.class));
            return evaluationContext;
        } catch (NoSuchMethodException nsme) {
            throw new IllegalArgumentException(nsme);
        }
    }
}