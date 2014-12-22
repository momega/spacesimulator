/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.media.opengl.GLAutoDrawable;

import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.renderer.DelayedActionEvent;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.swing.NewUserPointPanel;
import com.momega.spacesimulator.swing.SwingUtils;

/**
 * @author martin
 *
 */
public class UserPointController extends AbstractController {
	
	public static final String NEW_USER_POINT = "new_user_point";

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount()>1) {
            fireDelayedAction(e);
        }
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (NEW_USER_POINT.equals(e.getActionCommand())) {
			NewUserPointPanel panel = new NewUserPointPanel();
			SwingUtils.openDialog(panel.creatDialog("New User Point..."));
		}
	}
	
	@Override
	public void delayedActionPeformed(DelayedActionEvent delayed) {
		if (delayed.getEvent() instanceof MouseEvent) {
			MouseEvent e = (MouseEvent) delayed.getEvent();
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount()>1) {
				GLAutoDrawable drawable = delayed.getDrawable();
				Point position = GLUtils.getPosition(e);
				RendererModel.getInstance().createUserPoint(drawable, position);
			}
		}
	}

}
