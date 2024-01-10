package com.leaf.puzzle15;

import androidx.annotation.Nullable;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Coordinate)
            return x == ((Coordinate) obj).x && y == ((Coordinate) obj).y;
        return false;
    }
}
