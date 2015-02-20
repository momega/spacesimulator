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

}
