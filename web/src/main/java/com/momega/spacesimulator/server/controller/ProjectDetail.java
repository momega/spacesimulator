/**
 * 
 */
package com.momega.spacesimulator.server.controller;

import com.momega.spacesimulator.model.Timestamp;

/**
 * @author martin
 *
 */
public class ProjectDetail {

	private int id;
	private String name;
	private Timestamp time;
	private boolean running;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public boolean isRunning() {
		return running;
	}

}
