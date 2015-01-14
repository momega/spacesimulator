package com.momega.spacesimulator.renderer;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import org.apache.commons.math3.util.FastMath;

import com.jogamp.common.nio.Buffers;
import com.momega.spacesimulator.model.Vector3d;

public class VBO {
	
	private static final int VERTEX_SIZE = 3;
	
	private int[] handles;
	private int length;
	
	public static VBO createVBOEllipse(GL2 gl, double a, double b, int num_segments, double[] color) {
        double DEG2RAD = 2.0 * Math.PI / num_segments;
        List<Vector3d> list = new ArrayList<>();
        for (int i=0; i<=num_segments ; i++) {
            double degInRad = DEG2RAD * i;
            list.add(new Vector3d(FastMath.cos(degInRad) * a, FastMath.sin(degInRad) * b, 0));
        }
        return createVBO(gl, list, color);
    }
	
	public static VBO createVBO(GL2 gl, List<Vector3d> vertices, double[] color) {
		DoubleBuffer b = DoubleBuffer.allocate(VERTEX_SIZE * vertices.size() * 2);
		for(Vector3d vertex : vertices) {
			b.put(vertex.asArray());
			b.put(color);
		}
	    
	    DoubleBuffer vertex_data = Buffers.newDirectDoubleBuffer(b.array());
	    vertex_data.flip();

	    int[] handles = new int[1];
	    gl.glGenBuffers(1, handles, 0);
	    int vbo_vertex_handle = handles[0];
	    
	    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo_vertex_handle);
	    gl.glBufferData(GL.GL_ARRAY_BUFFER, Buffers.SIZEOF_DOUBLE * vertex_data.capacity(), vertex_data, GL.GL_STATIC_DRAW);
	    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
	    
	    return new VBO(handles, vertices.size());
	}

	public VBO(int[] handles, int length) {
		this.handles = handles;
		this.length = length;
	}
	
	public void draw(GL2 gl, int glType) {
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, handles[0]);

		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
		
		gl.glVertexPointer(VERTEX_SIZE, GL2.GL_DOUBLE, 6 * Buffers.SIZEOF_DOUBLE, 0l);
		gl.glColorPointer(VERTEX_SIZE, GL2.GL_DOUBLE, 6 * Buffers.SIZEOF_DOUBLE, 3 * Buffers.SIZEOF_DOUBLE);

		gl.glDrawArrays(glType, 0, length);

		gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);		
	}
	
	public void dispose(GL2 gl) {
		gl.glDeleteBuffers(1, handles, 0);		
	}

}
