package com.aregcraft.reforging.math;

public final class Matrix {
    private final double[][] array;

    public static Matrix scale(Vector scale) {
        return new Matrix(scale.x(), 0, 0,
                0, scale.y(), 0,
                0, 0, scale.z());
    }

    public static Matrix rotate(double angle, Vector axis) {
        var a = axis.normalize();
        var c = Math.cos(angle);
        var s = Math.sin(angle);
        var c1 = 1 - c;
        return new Matrix(c1 * a.x() * a.x() + c,
                c1 * a.x() * a.y() - a.z() * s,
                c1 * a.x() * a.z() + a.y() * s,
                c1 * a.x() * a.y() + a.z() * s,
                c1 * a.y() * a.y() + c,
                c1 * a.y() * a.z() - a.x() * s,
                c1 * a.x() * a.z() - a.y() * s,
                c1 * a.y() * a.z() + a.x() * s,
                c1 * a.z() * a.z() + c);
    }

    public static Matrix changeOfBasis(Vector i, Vector j, Vector k) {
        return new Matrix(i.x(), j.x(), k.x(),
                i.y(), j.y(), k.y(),
                i.z(), j.z(), k.z());
    }

    public Matrix(double a, double b, double c, double d, double e, double f, double g, double h, double i) {
        array = new double[][]{
                new double[]{a, b, c},
                new double[]{d, e, f},
                new double[]{g, h, i}
        };
    }

    public double get(int row, int column) {
        return array[row][column];
    }

    public void set(int row, int column, double value) {
        array[row][column] = value;
    }

    public Matrix add(Matrix other) {
        var blank = Matrix.scale(new Vector(0, 0, 0));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                blank.set(i, j, get(i, j) + other.get(i, j));
            }
        }
        return blank;
    }

    public Matrix multiply(Matrix other) {
        var blank = Matrix.scale(new Vector(0, 0, 0));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    blank.set(i, j, blank.get(i, j) + get(i, k) * other.get(k, j));
                }
            }
        }
        return blank;
    }

    public Matrix multiply(double scalar) {
        var blank = Matrix.scale(new Vector(0, 0, 0));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                blank.set(i, j, blank.get(i, j) * scalar);
            }
        }
        return blank;
    }
}
