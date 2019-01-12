package com.codingame.antiyoy;

// Grants access to Constants.CONSTANTE
import static com.codingame.antiyoy.Constants.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GameState {
    private  Cell[][] map;

    public ArrayList<Unit> units = new ArrayList<>();

    public ArrayList<AtomicInteger> playerGolds = new ArrayList<>();

    public GameState() {
        // create full map
        this.map = new Cell[MAP_WIDTH][MAP_HEIGHT];
        for(int x = 0; x < MAP_WIDTH; ++x)
            for (int y = 0; y < MAP_HEIGHT; ++y)
                this.map[x][y] = new Cell(x, y);

        for (int i = 0; i < PLAYER_COUNT; ++i)
            this.playerGolds.add(new AtomicInteger(2 * UNIT_COST[1]));
    }

    public boolean isInside(int x, int y) {
        return x >= 0 && x < MAP_WIDTH && y >= 0 && y < MAP_HEIGHT;
    }
    public boolean isLegal(int x, int y) {
        return isInside(x, y) && this.map[x][y].getOwner() != VOID;
    }

    public void computeNeighbours() {
        for (int x = 0; x < MAP_WIDTH; ++x) {
            for (int y = 0; y < MAP_HEIGHT; ++y) {
                if (isInside(x, y-1) && map[x][y-1].getOwner() != VOID)
                    map[x][y].neighbours[UP] = map[x][y-1];
                if (isInside(x+1, y) && map[x+1][y].getOwner() != VOID)
                    map[x][y].neighbours[RIGHT] = map[x+1][y];
                if (isInside(x, y+1) && map[x][y+1].getOwner() != VOID)
                    map[x][y].neighbours[DOWN] = map[x][y+1];
                if (isInside(x-1, y) && map[x-1][y].getOwner() != VOID)
                    map[x][y].neighbours[LEFT] = map[x-1][y];
            }
        }
    }

    public void endTurn() {
        // increments golds of player
        for (int x = 0; x < MAP_WIDTH; ++x) {
            for (int y = 0; y < MAP_HEIGHT; ++y) {
                if (map[x][y].getOwner() >=0)
                    this.playerGolds.get(map[x][y].getOwner()).addAndGet(CELL_INCOME);
            }
        }

        // decrement for units
        for (Unit unit : this.units) {
            this.playerGolds.get(unit.getOwner()).addAndGet(- UNIT_UPKEEP[unit.getLevel()]);
        }
    }

    public Cell getCell(int x, int y) { return this.map[x][y]; }
    public Cell getSymmetricCell(int x, int y) { return this.map[MAP_WIDTH - x - 1][MAP_HEIGHT - y - 1]; }

    public int getUnitsSize() {
        return this.units.size();
    }
    public Unit getUnit(int id) {
        for (Unit unit : this.units) {
            if (unit.getId() == id)
                return unit;
        }
        return null;
    }

    public int getGold(int idx) {
        if (idx < 0 || idx >= PLAYER_COUNT)
                return -1;
        return playerGolds.get(idx).intValue();
    }

    public void addUnit(Unit unit) {
        this.units.add(unit);
        this.map[unit.getX()][unit.getY()].setOwner(unit.getOwner());
        this.map[unit.getX()][unit.getY()].setUnit(unit);
        this.playerGolds.get(unit.getOwner()).addAndGet(-10);
    }

    public boolean moveUnit(int idx, Cell position) {
        if (idx >= this.units.size())
            return false;

        Unit unit = this.units.get(idx);
        unit.setX(position.getX());
        unit.setY(position.getY());
        map[unit.getX()][unit.getY()].setOwner(unit.getOwner());
        return true;
    }

    public void moveUnit(Unit unit, int x, int y) {
        // free current cell
        map[unit.getX()][unit.getY()].setUnit(null);

        unit.setX(x);
        unit.setY(y);
        map[unit.getX()][unit.getY()].setOwner(unit.getOwner());
        // occupy new cell
        map[unit.getX()][unit.getY()].setUnit(unit);
    }

}