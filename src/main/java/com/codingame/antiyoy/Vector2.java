package com.codingame.antiyoy;

public class Vector2 {
    private int x;
    private int y;

    public Vector2() {
        this.x = -1;
        this.y = -1;
    }

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public void add(int x, int y) { this.x += x; this.y += y;}
}