package com.momega.spacesimulator.model;

/**
 * The class represents 3x3 matrix used for coordinate transformation
 * Created by martin on 7/15/14.
 */
public class Matrix3d {

    private Vector3d[] rows = new Vector3d[3];

    public Matrix3d(Vector3d row1, Vector3d row2, Vector3d row3) {
        this.rows[0] = row1;
        this.rows[1] = row2;
        this.rows[2] = row3;
    }

    public Vector3d getRow(int row) {
        return this.rows[row];
    }

    public Vector3d getColumn(int column) {
        if (column==0) {
            return new Vector3d(rows[0].x, rows[1].x, rows[2].x);
        } else if (column==1) {
            return new Vector3d(rows[0].y, rows[1].y, rows[2].y);
        } else if (column==2) {
            return new Vector3d(rows[0].z, rows[1].z, rows[2].z);
        }
        throw new IllegalArgumentException("illegal argument number");
    }

    public Vector3d multiple(Vector3d vector) {
        double x = getRow(0).dot(vector);
        double y = getRow(1).dot(vector);
        double z = getRow(2).dot(vector);
        return new Vector3d(x, y, z);
    }

    public Matrix3d multiple(Matrix3d matrix) {
        Vector3d column1 = multiple(matrix.getColumn(0));
        Vector3d column2 = multiple(matrix.getColumn(1));
        Vector3d column3 = multiple(matrix.getColumn(2));
        return new Matrix3d(column1, column2, column3).transpose();
    }

    /**
     * Transpose of the matrix
     * @return new transposed matrix
     */
    public Matrix3d transpose() {
        return new Matrix3d(getColumn(0), getColumn(1), getColumn(2));
    }

}
