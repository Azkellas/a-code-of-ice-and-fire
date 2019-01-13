package com.codingame.antiyoy;

// Grants access to Constants.CONSTANTE
import static com.codingame.antiyoy.Constants.*;

import com.codingame.game.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
public class GameState {
    private  Cell[][] map;

    public List<Building> HQs = new ArrayList<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    private Map<Integer, Unit> units = new HashMap<>();

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

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < MAP_WIDTH && y >= 0 && y < MAP_HEIGHT;
    }
    public boolean isInside(int x, int y) {
        return isWithinBounds(x, y) && this.map[x][y].getOwner() != VOID;
    }

    private void computeNeighbours() {
        for (int x = 0; x < MAP_WIDTH; ++x) {
            for (int y = 0; y < MAP_HEIGHT; ++y) {
                if (isInside(x, y-1))
                    map[x][y].neighbours[UP] = map[x][y-1];
                if (isInside(x+1, y))
                    map[x][y].neighbours[RIGHT] = map[x+1][y];
                if (isInside(x, y+1))
                    map[x][y].neighbours[DOWN] = map[x][y+1];
                if (isInside(x-1, y))
                    map[x][y].neighbours[LEFT] = map[x-1][y];
            }
        }
    }

    private void computeIncome(int playerId) {
        // increments golds of player
        for (int x = 0; x < MAP_WIDTH; ++x) {
            for (int y = 0; y < MAP_HEIGHT; ++y) {
                if (map[x][y].getOwner() == playerId && map[x][y].isActive())
                    this.playerGolds.get(playerId).addAndGet(CELL_INCOME);
            }
        }

        // decrement for units
        for (Unit unit : this.units.values()) {
            if (unit.getOwner() == playerId && unit.isAlive())
                this.playerGolds.get(playerId).addAndGet(- UNIT_UPKEEP[unit.getLevel()]);
        }

        // Negative amount of gold: kill all units and reset to 0
        if (this.playerGolds.get(playerId).intValue() < 0) {
            this.playerGolds.get(playerId).set(0);

            List<Unit> toKill = new ArrayList<>();
            this.units.forEach((key, unit)-> {if (unit.getOwner() == playerId) toKill.add(unit); });
            killUnits(toKill);
        }
    }

    private void killUnit(Unit unit) {
        unit.doDispose();
        this.units.remove(unit.getId());
    }

    public Cell getCell(int x, int y) { return this.map[x][y]; }
    private Cell getSymmetricCell(int x, int y) { return this.map[MAP_WIDTH - x - 1][MAP_HEIGHT - y - 1]; }

    public Unit getUnit(int id) {
        return this.units.get(id);
    }

    public int getGold(int idx) {
        if (idx < 0 || idx >= PLAYER_COUNT)
                return -1;
        return playerGolds.get(idx).intValue();
    }

    public void addUnit(Unit unit) {
        this.units.put(unit.getId(), unit);
        unit.getCell().setOwner(unit.getOwner());
        unit.getCell().setUnit(unit);
        this.playerGolds.get(unit.getOwner()).addAndGet(-UNIT_COST[unit.getLevel()]);
    }

    public void moveUnit(Unit unit, Cell newPosition) {
        // free current cell
        unit.getCell().setUnit(null);

        unit.moved();
        unit.setX(newPosition.getX());
        unit.setY(newPosition.getY());
        unit.setCell(newPosition);

        newPosition.setOwner(unit.getOwner());
        // occupy new cell
        newPosition.setUnit(unit);
    }

    public void initTurn(int playerId) {
        this.computeActiveCells(playerId);
        this.killSeparatedUnits(playerId);
        this.computeIncome(playerId);
        this.units.forEach( (key, unit) -> { if (unit.getOwner() == playerId) unit.newTurn(); });
    }

    public void computeAllActiveCells() {
        for (int playerId = 0; playerId < PLAYER_COUNT; ++playerId)
            this.computeActiveCells(playerId);
    }

    private void computeActiveCells(int playerId) {
        // Set all inactive
        for (int x = 0; x < MAP_WIDTH; ++x) {
            for (int y = 0; y < MAP_HEIGHT; ++y) {
                if (this.map[x][y].getOwner() == playerId)
                    this.map[x][y].setInactive();
            }
        }

        ArrayList<Cell> queue = new ArrayList<>();
        Cell start = this.HQs.get(playerId).getCell();
        queue.add(start);
        start.setActive();

        // Reactivate cells connected to starting point
        while (!queue.isEmpty()) {
            Cell currentCell = queue.get(0);
            queue.remove(0);
            for (Cell cell : currentCell.neighbours) {
                if (cell != null && !cell.isActive() && cell.getOwner() == playerId) {
                    cell.setActive();
                    queue.add(cell);
                }
            }
        }
    }

    private void killSeparatedUnits(int playerId) {
        List<Unit> toKill = new ArrayList<>();
        this.units.forEach((key, unit)-> {if (unit.getOwner() == playerId && !unit.getCell().isActive()) toKill.add(unit); });
        killUnits(toKill);
    }

    private void killUnits(List<Unit> units) {
        units.forEach(unit -> killUnit(unit));
    }


    private void sendMap(Player player) {
        for (int y = 0; y < MAP_HEIGHT; ++y) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < MAP_WIDTH; ++x) {
                int owner = this.map[x][y].getOwner();
                if (owner >= 0)
                    owner = (owner  - player.getIndex() + PLAYER_COUNT) % PLAYER_COUNT; // own unit = owner 0
                line.append(owner);
                if (x != MAP_WIDTH - 1) {
                    line.append(" ");
                }
            }
            player.sendInputLine(line.toString());
        }
    }

    private void sendUnits(Player player) {
        // send unit count
        player.sendInputLine(String.valueOf(this.units.size()));

        // send units
        this.units.forEach((id, unit) -> {
            StringBuilder line = new StringBuilder();
            line.append(unit.getId())
                    .append(" ")
                    .append( (unit.getOwner() - player.getIndex() + PLAYER_COUNT) % PLAYER_COUNT) // always 0 for the player
                    .append(" ")
                    .append(unit.getLevel())
                    .append(" ")
                    .append(unit.getX())
                    .append(" ")
                    .append(unit.getY());
            player.sendInputLine(line.toString());
        });
    }

    public void sendState(Player player) {
        // send gold
        player.sendInputLine(String.valueOf(this.playerGolds.get(player.getIndex())));

        sendMap(player);
        sendUnits(player);
    }

    public void generateMap() {
        for (int x = 0; x < MAP_WIDTH; ++x) {
            for (int y = 0; y < MAP_HEIGHT; ++y) {
                // do not modify nearby HQs cells
                if (x + y <= 3)
                    continue;
                if (MAP_WIDTH - x + MAP_HEIGHT - y <= 3)
                    continue;

                double random = Math.random() * 100;
                int owner = random < 20 ? -2 : -1;
                this.map[x][y].setOwner(owner);
                this.getSymmetricCell(x, y).setOwner(owner);
            }
        }
        // Restore HQs cells
        this.computeNeighbours();
    }

    public void createHQs(int playersCount) throws Exception {
        if (playersCount != 2) {
            throw new Exception("More than 2 players mode not implemented");
        }

        // Build players HQs
        Building HQ0 = new Building(this.map[0][0], 0, BUILDING_TYPE.HQ);
        Building HQ1 = new Building(this.map[MAP_WIDTH-1][MAP_HEIGHT-1], 1, BUILDING_TYPE.HQ);
        this.HQs.add(HQ0);
        this.HQs.add(HQ1);
        HQ0.getCell().setOwner(0);
        HQ1.getCell().setOwner(1);
    }
}