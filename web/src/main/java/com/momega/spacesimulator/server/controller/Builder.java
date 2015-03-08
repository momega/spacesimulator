/**
 * 
 */
package com.momega.spacesimulator.server.controller;

/**
 * @author martin
 *
 */
public class Builder {

	private int id;
	private String name;
	private String builderClassName;
	private String fileName;
	private byte[] data;
	
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
	
	public void setBuilderClassName(String builderClassName) {
		this.builderClassName = builderClassName;
	}
	
	public String getBuilderClassName() {
		return builderClassName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}

}
