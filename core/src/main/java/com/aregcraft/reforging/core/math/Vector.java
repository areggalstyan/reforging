package com.aregcraft.reforging.core.math;

import org.bukkit.Location;

public record Vector(double x, double y, double z) {
    public static final Vector EMPTY = new Vector(0, 0, 0);

    public Vector(org.bukkit.util.Vector vector) {
        this(vector.getX(), vector.getY(), vector.getZ());
    }

    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y, z + other.z);
    }

    public Location at(Location location) {
        return location.add(toBukkit());
    }

    public Vector add(double scalar) {
        return add(new Vector(scalar, scalar, scalar));
    }

    public Vector subtract(Vector other) {
        return add(other.negate());
    }

    public Vector subtract(double scalar) {
        return add(-scalar);
    }

    public Vector multiply(double scalar) {
        return new Vector(x * scalar, y * scalar, z * scalar);
    }

    public Vector multiply(Matrix matrix) {
        return new Vector(multiplyRow(matrix, 0),
                multiplyRow(matrix, 1),
                multiplyRow(matrix, 2));
    }

    private double multiplyRow(Matrix matrix, int row) {
        return matrix.get(row, 0) * x
                + matrix.get(row, 1) * y
                + matrix.get(row, 2) * z;
    }

    public Vector divide(double scalar) {
        return multiply(1 / scalar);
    }

    public Vector cross(Vector other) {
        return new Vector(y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x);
    }

    public Vector negate() {
        return multiply(-1);
    }

    public Vector normalize() {
        return divide(magnitude());
    }

    public double dot(Vector other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public double distance(Vector other) {
        var difference = subtract(other);
        return Math.sqrt(difference.dot(difference));
    }

    public double magnitude() {
        return distance(new Vector(0, 0, 0));
    }

    public org.bukkit.util.Vector toBukkit() {
        return new org.bukkit.util.Vector(x, y, z);
    }
}
