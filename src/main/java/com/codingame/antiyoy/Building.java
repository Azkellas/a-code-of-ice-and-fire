package com.codingame.antiyoy;

import com.codingame.antiyoy.view.BuildingView;

import static com.codingame.antiyoy.Constants.*;

public class Building extends Entity {
    private BUILDING_TYPE type;

    private Cell cell;

    private BuildingView viewer;

    public Building(int x, int y, int ownerId, BUILDING_TYPE type) {
        super(x, y, ownerId);
        this.type = type;

        this.cell = null;
    }

    public BUILDING_TYPE getType() { return this.type; }

    public void setCell(Cell cell) {this.cell = cell; }
    public Cell getCell() { return this.cell; }

    public void setViewer(BuildingView viewer) { this.viewer = viewer; }
    public void doDispose() {
        this.cell.setUnit(null);
//        this.alive = false;
        this.viewer.doDispose();
    }
}