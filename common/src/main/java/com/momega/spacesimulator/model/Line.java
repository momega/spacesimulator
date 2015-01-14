/**
 * 
 */
package com.momega.spacesimulator.model;

/**
 * @author martin
 *
 */
public class Line {

	private final Vector3d origin;
	private final Vector3d direction;
	
	public static Line from2Points(Vector3d origin, Vector3d other) {
		return new Line(origin, other.subtract(origin));
	}
	
	public Line(Vector3d origin, Vector3d direction) {
		this.origin = origin;
		this.direction = direction.normalize();
	}

	public Vector3d getOrigin() {
		return origin;
	}
	
	public Vector3d getDirection() {
		return direction;
	}
	
	public Line move(Vector3d m) {
		return new Line(getOrigin().add(m), getDirection());
	}
	
	@Override
	public String toString() {
		return "origin = " + getOrigin().toString() + " direction = " + getDirection().toString();
	}

}
