/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import com.momega.spacesimulator.opengl.GLUtils;
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
			Point position = GLUtils.getPosition(e);
			RendererModel.getInstance().setNewUserPointPosition(position);
        }
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (NEW_USER_POINT.equals(e.getActionCommand())) {
			NewUserPointPanel panel = new NewUserPointPanel();
			SwingUtils.openDialog(panel.creatDialog("New User Point..."));
		}
	}

}
