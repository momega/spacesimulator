/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * @author martin
 *
 */
public abstract class DefaultDialog extends JDialog {

	private static final long serialVersionUID = -8033588625254380734L;

	protected DefaultDialog(String title) {
        super((Frame)null, title, true);

        getContentPane().setLayout(new BorderLayout());

        JPanel mainPanel = createMainPanel();

        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("Ok");
        okButton.setIcon(SwingUtils.createImageIcon("/images/accept.png"));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (okPressed()) {
            		setVisible(false);
            	}
            }
        });
        buttonsPanel.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setIcon(SwingUtils.createImageIcon("/images/cancel.png"));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonsPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.PAGE_END);

        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
	}
	
	protected abstract JPanel createMainPanel();
	
	protected abstract boolean okPressed();

}
