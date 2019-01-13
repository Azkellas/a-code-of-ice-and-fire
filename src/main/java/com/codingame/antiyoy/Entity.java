package com.codingame.antiyoy;

public class Entity extends Vector2 {
    private int owner;

    public Entity() {
        super();
    }

    public Entity(int x, int y) {
        super(x, y);
    }

    public Entity(int x, int y, int owner) {
        super(x, y);
        this.owner = owner;
    }
    public int getOwner() { return this.owner; }
    public void setOwner(int owner) { this.owner = owner; }
}