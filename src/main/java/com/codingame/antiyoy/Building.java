package com.codingame.antiyoy;

import com.codingame.antiyoy.view.BuildingView;

import static com.codingame.antiyoy.Constants.*;

public class Building extends Entity {
    private BUILDING_TYPE type;

    private Cell cell;

    private BuildingView viewer;

    public Building(Cell cell, int ownerId, BUILDING_TYPE type) {
        super(cell.getX(), cell.getY(), ownerId);
        this.type = type;
        this.cell = cell;
    }

    public BUILDING_TYPE getType() { return this.type; }
    public int getIntType() {
        if (this.type == BUILDING_TYPE.HQ)
            return 0;
        if (this.type == BUILDING_TYPE.MINE)
            return 1;
        if (this.type == BUILDING_TYPE.TOWER)
            return 2;
        return -1;  // impossible case
    }

    public Cell getCell() { return this.cell; }

    public void setViewer(BuildingView viewer) { this.viewer = viewer; }

    public void doDispose() {
        this.viewer.doDispose();
    }

    static public BUILDING_TYPE convertType(String string) {
        if (string.equals("MINE"))
            return BUILDING_TYPE.MINE;
        else  // (string.equals("TOWER"))
            return BUILDING_TYPE.TOWER;
    }
}