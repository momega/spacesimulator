/**
 * 
 */
package com.momega.spacesimulator.model;

/**
 * @author martin
 *
 */
public class Plane {

	private final Vector3d origin;
	private final Vector3d normal;
	
	public Plane(Vector3d origin, Vector3d normal) {
		super();
		this.origin = origin;
		this.normal = normal.normalize();
	}

	public Vector3d getOrigin() {
		return origin;
	}
	
	public Vector3d getNormal() {
		return normal;
	}
	
	/**
	 * Builds intersection of the two planes. The origin is used to setup new origin 
	 * of result line
	 * @param other the other plane
	 * @param helper the helper point
	 * @return
	 */
	public Line intersection(Plane other, Vector3d helper) {
		double d1 = -normal.dot(getOrigin());
        double a1 = normal.getX();
        double b1 = normal.getY();
        double c1 = normal.getZ();
        
        double d2 = -other.getNormal().dot(other.getOrigin());
        double a2 = other.getNormal().getX();
        double b2 = other.getNormal().getY();
        double c2 = other.getNormal().getZ();
        
        Vector3d p = normal.cross(other.getNormal()).normalize();
        
        double x = helper.getX();
        double y = (a2*x - c2*a1*x/c1 - c2*d1/c1 + d2) / (c2*b1/c1 - b2);
        double z = -(a1*x + b1*y + d1) / c1;
        
        return new Line(new Vector3d(x, y, z), p);
	}
	
	@Override
	public String toString() {
		return "origin = " + getOrigin().toString() + " normal = " + getNormal().toString();
	}

}
