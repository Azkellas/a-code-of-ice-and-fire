package com.codingame.antiyoy;

import com.codingame.antiyoy.view.UnitView;


public class Unit {
    private int id;
    private int x;
    private int y;
    private int ownerId;  // -1 if neutral, -2 if void
    private int level;

    private static int unitIdCount = 0;

    public Unit(int x, int y, int ownerId, int level) {
        this.x = x;
        this.y = y;

        // Grant a unique id
        this.id = unitIdCount;
        unitIdCount++;

        this.ownerId = ownerId;
        this.level = level;
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getId() { return this.id; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getOwner() { return this.ownerId; }
    public int getLevel() { return this.level; }
}