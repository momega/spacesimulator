package com.momega.spacesimulator.controller;

import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import com.momega.spacesimulator.renderer.DelayedActionEvent;

/**
 * The base controller interface
 * Created by martin on 4/19/14.
 */
public interface Controller extends KeyListener, MouseListener, 
	MouseMotionListener, MouseWheelListener, ComponentListener, ActionListener {
	
	/**
	 * Perform delayed actions
	 * @param e the event
	 */
	void delayedActionPeformed(DelayedActionEvent e);
}
