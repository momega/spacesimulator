package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.renderer.RendererModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The dialog of the preferences
 * Created by martin on 11/2/14.
 */
public class PreferencesDialog extends JDialog {

    private JCheckBox chkBeams;

    public PreferencesDialog() {
        super((Frame)null, "Preferences...", true);

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(7,1));

        chkBeams = new JCheckBox("Show equatorial plane around selected planet");
        mainPanel.add(chkBeams);
        chkBeams.setSelected(RendererModel.getInstance().isBeamsShowed());

        setContentPane(rootPanel);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RendererModel.getInstance().setBeamsShowed(chkBeams.isSelected());
                setVisible(false);
            }
        });
        buttonsPanel.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonsPanel.add(cancelButton);

        rootPanel.add(mainPanel, BorderLayout.CENTER);
        rootPanel.add(buttonsPanel, BorderLayout.PAGE_END);

        setPreferredSize(new Dimension(600, 400));
        pack();
    }
}
