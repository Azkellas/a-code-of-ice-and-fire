package com.codingame.antiyoy;

import com.codingame.antiyoy.view.CellView;

import com.codingame.antiyoy.Unit;

public class Cell {
    private int x;
    private int y;
    private int ownerId;  // -1 if neutral, -2 if void
    private boolean active;
    private Unit unit;

    public Cell neighbours[];
    private CellView observer;
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.ownerId = -1;
        this.unit = null;
        this.active = true;
        this.neighbours = new Cell[4];
        for (int i = 0; i < 4; ++i)
            this.neighbours[i] = null;
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public Unit getUnit() { return this.unit; }
    public void setUnit(Unit unit) { this.unit = unit; }
    public int getOwner() { return this.ownerId; }
    public void setOwner(int newOwner) { this.ownerId = newOwner; }

    public boolean isFree() { return this.unit == null; }
    public boolean isActive() { return this.active; }
    public void setActive() { this.active = true; }
    public void setInactive() { this.active = false; }

    public boolean isPlayable(int playerId) {
        for (Cell neighbour : this.neighbours) {
            if (neighbour != null && neighbour.getOwner() == playerId && neighbour.isActive())
                return true;
        }
        return false;
    }
}