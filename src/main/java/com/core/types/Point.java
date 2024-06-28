package com.core.types;

import java.util.Objects;

/**
 * This class represents a coordinate X, Y to use with a HashSet
 */
public class Point {
    public Integer x, y;

    public Point(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point pair = (Point) o;
        return Objects.equals(x, pair.x) && Objects.equals(y, pair.y);
    }
}
