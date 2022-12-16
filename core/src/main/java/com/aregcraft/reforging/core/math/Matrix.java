package com.aregcraft.reforging.core.math;

public class Matrix {
    public static final Matrix EMPTY = new Matrix(0, 0, 0, 0, 0, 0, 0, 0, 0);

    private final double[][] array;

    public Matrix(double a, double b, double c, double d, double e, double f, double g, double h, double i) {
        array = new double[][]{
                new double[]{a, b, c},
                new double[]{d, e, f},
                new double[]{g, h, i}
        };
    }

    public static Matrix changeOfBasis(Vector i, Vector j, Vector k) {
        return new Matrix(i.x(), j.x(), k.x(),
                i.y(), j.y(), k.y(),
                i.z(), j.z(), k.z());
    }

    public double get(int row, int column) {
        return array[row][column];
    }

    public void set(int row, int column, double value) {
        array[row][column] = value;
    }

    public Matrix add(Matrix other) {
        var blank = Matrix.EMPTY;
        for (var i = 0; i < 3; i++) {
            for (var j = 0; j < 3; j++) {
                blank.set(i, j, get(i, j) + other.get(i, j));
            }
        }
        return blank;
    }

    public Matrix multiply(Matrix other) {
        var blank = Matrix.EMPTY;
        for (var i = 0; i < 3; i++) {
            for (var j = 0; j < 3; j++) {
                for (var k = 0; k < 3; k++) {
                    blank.set(i, j, blank.get(i, j) + get(i, k) * other.get(k, j));
                }
            }
        }
        return blank;
    }

    public Matrix multiply(double scalar) {
        var blank = Matrix.EMPTY;
        for (var i = 0; i < 3; i++) {
            for (var j = 0; j < 3; j++) {
                blank.set(i, j, blank.get(i, j) * scalar);
            }
        }
        return blank;
    }
}
