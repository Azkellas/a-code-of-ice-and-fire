package com.codingame.antiyoy;

import com.codingame.antiyoy.view.CellView;

import static com.codingame.antiyoy.Constants.*;

public class Cell extends Entity {
    private boolean active;
    private Unit unit;
    private Building building;

    private Cell neighbours[];

    public Cell(int x, int y) {
        super(x, y, -1);
        this.unit = null;
        this.building = null;
        this.active = true;
        this.neighbours = new Cell[4];
        for (int i = 0; i < 4; ++i)
            this.neighbours[i] = null;
    }

    public Unit getUnit() { return this.unit; }
    public void setUnit(Unit unit) { this.unit = unit; }

    public Building getBuilding() { return this.building; }
    public void setBuilding(Building building) { this.building = building; }

    public Cell[] getNeighbours() { return this.neighbours; }
    public void setNeighbour(int idx, Cell cell) { this.neighbours[idx] = cell; }

    public boolean isActive() { return this.active; }
    public void setActive() { this.active = true; }
    public void setInactive() { this.active = false; }

    public boolean isFree() { return this.unit == null && this.building == null; }
    public boolean isCapturable(int playerId, int level) {
        // not on enemy cells protected by towers
        if (this.getOwner() != playerId && this.isProtected() && level < CAPTURE_LEVEL)
            return false;

        // not protected and free
        if (this.isFree())
            return true;

        // On own cell: not (since not free)
        if (this.getOwner() == playerId)
            return false;


        // cannot kill enemy unit if level too small
        if (this.unit != null) {
            if (level != MAX_LEVEL && level < this.unit.getLevel())
                return false;
        }

        return true;
    }

    public boolean isProtected() {
        if (this.building != null && this.building.getType() == BUILDING_TYPE.TOWER)
            return true;

        for (Cell neighbour : this.neighbours) {
            if (neighbour == null || neighbour.getOwner() != this.getOwner())
                continue;
            Building neighbourBuilding = neighbour.getBuilding();
            if (neighbourBuilding != null && neighbourBuilding.getType() == BUILDING_TYPE.TOWER)
                return true;
        }
        return false;
    }

    public boolean isPlayable(int playerId) {
        // return true iif owned and active or next to an owned and active cell
        if (this.getOwner() == playerId && this.isActive()) {
            return true;
        }
        for (Cell neighbour : this.neighbours) {
            if (neighbour != null && neighbour.getOwner() == playerId && neighbour.isActive())
                return true;
        }
        return false;
    }
}