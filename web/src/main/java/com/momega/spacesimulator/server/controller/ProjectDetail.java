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

	private String id;
	private String name;
	private Timestamp time;
	private boolean running;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
