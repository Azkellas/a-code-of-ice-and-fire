package com.codingame.antiyoy;

import com.codingame.antiyoy.view.CellView;

import static com.codingame.antiyoy.Constants.*;

public class Cell extends Entity {
    private boolean active;
    private Unit unit;

    private Cell neighbours[];

    public Cell(int x, int y) {
        super(x, y, -1);
        this.unit = null;
        this.active = true;
        this.neighbours = new Cell[4];
        for (int i = 0; i < 4; ++i)
            this.neighbours[i] = null;
    }

    public Unit getUnit() { return this.unit; }
    public void setUnit(Unit unit) { this.unit = unit; }

    public Cell[] getNeighbours() { return this.neighbours; }
    public void setNeighbour(int idx, Cell cell) { this.neighbours[idx] = cell; }

    public boolean isActive() { return this.active; }
    public void setActive() { this.active = true; }
    public void setInactive() { this.active = false; }

    public boolean isFree() { return this.unit == null; }
    public boolean isCapturable(int level) { return this.isFree() || level == MAX_LEVEL || level > this.unit.getLevel(); }

    public boolean isPlayable(int playerId) {
        for (Cell neighbour : this.neighbours) {
            if (neighbour != null && neighbour.getOwner() == playerId && neighbour.isActive())
                return true;
        }
        return false;
    }
}