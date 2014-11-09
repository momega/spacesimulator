/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.swing.NewUserPointDialog;
import com.momega.spacesimulator.swing.SwingUtils;

/**
 * @author martin
 *
 */
public class UserPointController extends AbstractController {
	
	public static final String NEW_USER_POINT = "new_user_point";

	@Override
	public void mouseClicked(MouseEvent e) {
		Point position = GLUtils.getPosition(e);
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount()>1) {
            RendererModel.getInstance().setMouseCoordinates(position);
        }
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (NEW_USER_POINT.equals(e.getActionCommand())) {
			NewUserPointDialog dialog = new NewUserPointDialog();
			SwingUtils.openDialog(dialog);
		}
	}

}
