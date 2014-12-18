package com.momega.spacesimulator.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
 
public class JStatusBar extends JPanel {
 
    private static final long serialVersionUID = 1L;
 
    protected JPanel leftPanel;
    protected JPanel rightPanel;
 
    public JStatusBar() {
        createPartControl();
    }
 
    protected void createPartControl() {    
    	setLayout(new BorderLayout());
    	setBorder(new BevelBorder(BevelBorder.LOWERED));
        setPreferredSize(new Dimension(getWidth(), 23));
        
        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 3));
        leftPanel.setOpaque(false);
        add(leftPanel, BorderLayout.WEST);
 
        rightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 5, 3));
        rightPanel.setOpaque(false);
        add(rightPanel, BorderLayout.EAST);
    }
 
    public void setLeftComponent(JComponent component) {
        leftPanel.add(component);
    }
 
    public void addRightComponent(JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(
                FlowLayout.LEADING, 5, 0));
        panel.add(new SeparatorPanel(Color.GRAY, Color.WHITE));
        panel.add(component);
        rightPanel.add(panel);
    }
    
    public class SeparatorPanel extends JPanel {
    	 
        private static final long serialVersionUID = 1L;
     
        protected Color leftColor;
        protected Color rightColor;
     
        public SeparatorPanel(Color leftColor, Color rightColor) {
            this.leftColor = leftColor;
            this.rightColor = rightColor;
            setOpaque(false);
        }
     
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(leftColor);
            g.drawLine(0, 0, 0, getHeight());
            g.setColor(rightColor);
            g.drawLine(1, 0, 1, getHeight());
        }
     
    }
 
}