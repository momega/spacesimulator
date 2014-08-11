package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.NamedObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by martin on 8/11/14.
 */
public class DetailDialog extends JDialog implements ActionListener {

    public DetailDialog(Frame parent, NamedObject namedObject) {
        super(parent, namedObject.getName(), true);
        if (parent != null) {
            Dimension parentSize = parent.getSize();
            Point p = parent.getLocation();
            setLocation(p.x + parentSize.width / 2, p.y + parentSize.height / 2);
        }
        JPanel messagePane = new JPanel();
        messagePane.add(new JLabel("Pako"));
        getContentPane().add(messagePane);
        JPanel buttonPane = new JPanel();
        JButton button = new JButton("OK");
        buttonPane.add(button);
        button.addActionListener(this);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
    }
}
