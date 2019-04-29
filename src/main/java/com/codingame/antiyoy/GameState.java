package com.codingame.antiyoy;

// Grants access to Constants.CONSTANTE
import static com.codingame.antiyoy.Constants.*;

import com.codingame.game.Player;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GameState {
    private  Cell[][] map;
    private long seed;
    private int nbMineSpots;
    //private Vector2[][] mineSpots;

    private List<Building> HQs = new ArrayList<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    private Map<Integer, Unit> units = new HashMap<>();

    private ArrayList<AtomicInteger> playerGolds = new ArrayList<>();
    private ArrayList<AtomicInteger> playerIncome = new ArrayList<>();

    private Pathfinding pathfinding;

    public GameState(long seed) {
        // create full map
        this.map = new Cell[MAP_WIDTH][MAP_HEIGHT];
        for(int x = 0; x < MAP_WIDTH; ++x)
            for (int y = 0; y < MAP_HEIGHT; ++y)
                this.map[x][y] = new Cell(x, y);

        for (int i = 0; i < PLAYER_COUNT; ++i) {
            this.playerGolds.add(new AtomicInteger(2 * UNIT_COST[1]));
            this.playerIncome.add(new AtomicInteger(1));
        }
        this.seed = seed;
        this.pathfinding = new Pathfinding();

        Random generator = new Random(seed);
        //if even number, fair distribution
        //if odd number, one is central (TODO)
        this.nbMineSpots = 2* Math.round((generator.nextInt(NUMBER_MINESPOTS_MAX - NUMBER_MINESPOTS_MIN + 1) + NUMBER_MINESPOTS_MIN)/2.0f);


    }

    // getters
    public Cell getCell(int x, int y) { return this.map[x][y]; }

    public Unit getUnit(int id) { return this.units.get(id); }

    public int getIncome(int idx) { return playerIncome.get(idx).intValue(); }
    public AtomicInteger getAtomicIncome(int idx) { return playerIncome.get(idx); }

    public int getGold(int idx) { return playerGolds.get(idx).intValue(); }
    public AtomicInteger getAtomicGold(int idx) { return playerGolds.get(idx); }

    // map creation methods
    private Cell getSymmetricCell(int x, int y) { return this.map[MAP_WIDTH - x - 1][MAP_HEIGHT - y - 1]; }

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
                    map[x][y].setNeighbour(UP, map[x][y-1]);
                if (isInside(x+1, y))
                    map[x][y].setNeighbour(RIGHT, map[x+1][y]);
                if (isInside(x, y+1))
                    map[x][y].setNeighbour(DOWN, map[x][y+1]);
                if (isInside(x-1, y))
                    map[x][y].setNeighbour(LEFT, map[x-1][y]);
            }
        }
    }

    public Cell getNextCell(Unit unit, Cell target) {
        return pathfinding.getNearestCell(this.map, unit, target);
    }

    public int getNbMineSpots() { return this.nbMineSpots; }

    /*********************************

     MAP GENERATOR STARTS HERE

     /*******************************/

    // returns the cell (x,y) considering border cases : out of bounds = NEUTRAL
    public int getValue(int x, int y) {
        if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT)
            return NEUTRAL;
        else
            return this.map[x][y].getOwner();
    }

    //returns the neighbourhood (list of values) of a cell located in (x,y)
    //we only care about how much NEUTRAL and how much VOID tiles
    public List<Integer> getNeighbourhood(int x, int y) {
        List<Integer> neighbourhood = new ArrayList<>();

        for (int ix = -1; ix < 2; ++ix) {
            for (int iy = -1; iy < 2; ++iy) {
                neighbourhood.add(getValue(x+ix,y+iy));
            }
        }
        return neighbourhood;
    }


    // Cellular automata
    public void updateMap() {
        // copy current map
        Cell [][] currentMap = new Cell[MAP_WIDTH][MAP_HEIGHT];

        for(int x = 0; x < MAP_WIDTH; x++) {
            for(int y=0; y< MAP_HEIGHT; y++) {
                currentMap[x][y] = new Cell(x, y);
                currentMap[x][y].setOwner(this.map[x][y].getOwner());
            }
        }



        // Then we apply the automata to the copied map 'currentMap'
        // before copying back to this.map
        for (int x = 0; x < MAP_WIDTH; ++x) {
            for (int y = 0; y < MAP_HEIGHT; ++y) {
                List<Integer> neighbourhood = getNeighbourhood(x,y);

                if(Collections.frequency(neighbourhood, NEUTRAL) >= MAPGENERATOR_T)
                    currentMap[x][y].setOwner(NEUTRAL);
                else
                    currentMap[x][y].setOwner(VOID);
            }
        }

        for(int x = 0; x < MAP_WIDTH; x++) {
            for(int y=0; y< MAP_HEIGHT; y++) {
                this.map[x][y].setOwner(currentMap[x][y].getOwner());
            }
        }
    }

    // we define here functions used in FindConnectedComponents()
    public boolean not_visited(int x, int y, Cell [][] visited) {
        //if coordinate is invalid, we don't want to visit it
        if (!isInside(x, y))
            return false;

        return visited[x][y].getOwner() == NEUTRAL;
    }

    public void dfs(int x, int y, List<Vector2> currentConnectedComponent, Cell [][] visited) {
        Vector2 currentPair = new Vector2(x,y);
        currentConnectedComponent.add(currentPair);

        visited[x][y].setOwner(2);

        if (not_visited(x-1, y, visited))
            dfs(x-1, y, currentConnectedComponent, visited);
        if (not_visited(x+1, y, visited))
            dfs(x+1, y, currentConnectedComponent, visited);
        if (not_visited(x, y-1, visited))
            dfs(x, y-1, currentConnectedComponent, visited);
        if (not_visited(x, y+1, visited))
            dfs(x, y+1, currentConnectedComponent, visited);
    }

    public List<List<Vector2>> findConnectedComponents() {
        // A connected component is a list of coordinates
        // And we store them in a list
        List<List<Vector2>> connectedComponents = new ArrayList<>();

        // We find the connected components with a depth first search
        // We copy the current map in a 'visited' variable
        // "seen" tiles will be marked as owner = 2, to differentiate between VOID and NEUTRAL
        Cell [][] visited = new Cell[MAP_WIDTH][MAP_HEIGHT];

        for(int x = 0; x < MAP_WIDTH; x++) {
            for(int y=0; y< MAP_HEIGHT; y++) {
                visited[x][y] = new Cell(x, y);
                visited[x][y].setOwner(this.map[x][y].getOwner());
            }
        }

        for (int x = 0; x < MAP_WIDTH; ++x) {
            for (int y = 0; y < MAP_HEIGHT; ++y) {
                if (visited[x][y].getOwner() == NEUTRAL)
                {
                    List<Vector2> currentConnectedComponent = new ArrayList<>();
                    dfs(x, y, currentConnectedComponent, visited);
                    connectedComponents.add(currentConnectedComponent);
                }
            }
        }


        return connectedComponents;

    }


    private void linkComponents(Random generator) {
        List<List<Vector2>> connectedComponents = findConnectedComponents();

        if (connectedComponents.size() == 1) {
            return;
        }

        System.err.println("We have " + connectedComponents.size() + " components");
        // we find the barycenter of each connected components
        List<Vector2> randomPoints = new ArrayList<>();

        for (int n = 0; n < connectedComponents.size(); n++) {
            // get random element from component
            int componentSize = connectedComponents.get(n).size();
            randomPoints.add(connectedComponents.get(n).get(generator.nextInt(componentSize)));
        }

        // To connect, we take a random pair of selected points and link them (with symmetry)
        for (int i = 0; i < randomPoints.size(); ++i) {
            for (int j = i+1; j < randomPoints.size(); ++j) {
                Vector2 node1 = randomPoints.get(i);
                Vector2 node2 = randomPoints.get(j);

                // Link on X axis first then Y axis
                int startX = Math.min(node1.getX(), node2.getX());
                int endX = Math.max(node1.getX(), node2.getX());

                int startY = Math.min(node1.getY(), node2.getY());
                int endY = Math.max(node1.getY(), node2.getY());


                for (int x = startX; x <= endX; ++x) {
                    this.map[x][node2.getY()].setOwner(NEUTRAL);
                    this.getSymmetricCell(x, node2.getY()).setOwner(NEUTRAL);
                }

                for (int y = startY; y <= endY; ++y) {
                    this.map[node1.getX()][y].setOwner(NEUTRAL);
                    this.getSymmetricCell(node1.getX(), y).setOwner(NEUTRAL);
                }
            }
        }



    }


    public void generateMap() {
        Random generator = new Random(this.seed);

        for (int x = 0; x < MAP_WIDTH; ++x) {
            for (int y = 0; y < MAP_HEIGHT; ++y) {
                if (generator.nextFloat() > MAPGENERATOR_R)
                    this.map[x][y].setOwner(NEUTRAL);
                else
                    this.map[x][y].setOwner(VOID);
            }
        }

        this.map[0][0].setOwner(NEUTRAL);
        this.map[1][0].setOwner(NEUTRAL);
        this.map[0][1].setOwner(NEUTRAL);
        this.map[1][1].setOwner(NEUTRAL);

        //always a mine spot next to the HQ (right for P1 and left for P2)
        this.map[1][0].setMineSpot();
        this.getSymmetricCell(1,0).setMineSpot();

        //apply automata
        for (int i=0; i < MAPGENERATOR_ITERATIONSAUTOMATA; ++i) {
            updateMap();
        }


        // invert VOID and TILE as the cellular automata generates more caves like maps
        for (int x = 0; x < MAP_WIDTH; ++x) {
            for (int y = 0; y < MAP_HEIGHT; ++y) {
                // + 2 to be 0 or 1
                // +1 then %2 to invert 0 and 1
                // -2 to get back to -2 or -1
                int inverted = ((this.map[x][y].getOwner()+2 +1)%2) - 2;
                this.map[x][y].setOwner(inverted);
            }
        }


        // Now, the map is generated, we then have to correct the symmetry.
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < x+1; y++) {
                this.map[MAP_WIDTH-1-x][MAP_HEIGHT-1-y].setOwner(this.map[x][y].getOwner());
            }
        }

        // grid 3x3 around base can never be VOID
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                this.map[i][j].setOwner(NEUTRAL);
                this.getSymmetricCell(i, j).setOwner(NEUTRAL);
            }
        }

        // Connect everything to have a playable map (only 1 connected component)
        linkComponents(generator);


        // generate mine spots (-2 because already 1 generated near HQ)
        for (int i=0; i < Math.round(this.nbMineSpots/2) -1; i++) {


            int randomX = generator.nextInt(MAP_WIDTH);
            int randomY = generator.nextInt(MAP_HEIGHT);

            while (this.map[randomX][randomY].getOwner() == VOID || this.map[randomX][randomY].isMineSpot() || randomX+randomY==0 || randomX+randomY==MAP_WIDTH+MAP_HEIGHT-2) {
                randomX = generator.nextInt(MAP_WIDTH);
                randomY = generator.nextInt(MAP_HEIGHT);
            }

            this.map[randomX][randomY].setMineSpot();
            this.getSymmetricCell(randomX, randomY).setMineSpot();
        }

        // Restore HQs cells
        this.computeNeighbours();

        // init pathfinding
        this.pathfinding.init(this.map);
    }


    /*********************************

        MAP GENERATOR ENDS HERE

    /*******************************/

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
        this.addBuilding(HQ0);
        this.addBuilding(HQ1);
    }


    public List<Building> getHQs() { return this.HQs; }


    // kill methods
    private void killUnit(Unit unit) {
        unit.die();
        unit.doDispose();
        this.units.remove(unit.getId());
    }

    private void clearCell(Cell cell) {
        if (cell.getUnit() != null) {
            killUnit(cell.getUnit());
            cell.setUnit(null);
        }
        if (cell.getBuilding() != null && cell.getBuilding().getType() != BUILDING_TYPE.HQ) {
            Building building = cell.getBuilding();
            building.doDispose();
            this.buildings.removeIf(building1 -> building1.getX() == building.getX() && building1.getY() == building.getY());
            cell.setBuilding(null);
        }
    }

    private void killUnits(List<Unit> units) {
        units.forEach(unit -> killUnit(unit));
    }

    // init turn methods
    public void initTurn(int playerId) {
        this.computeActiveCells(playerId);
        this.killSeparatedUnits(playerId);
        this.computeIncome(playerId);
        this.computeGold(playerId);
        if (this.playerGolds.get(playerId).intValue() < 0) {
            negativeGoldWipeout(playerId);
        }
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
            for (Cell cell : currentCell.getNeighbours()) {
                if (cell != null && !cell.isActive() && cell.getOwner() == playerId) {
                    cell.setActive();
                    queue.add(cell);
                }
            }
        }
    }


    private void killSeparatedUnits(int playerId) {
        List<Unit> toKill = new ArrayList<>();
        this.units.forEach((key, unit)-> {if (unit.isAlive() && unit.getOwner() == playerId && !unit.getCell().isActive()) toKill.add(unit); });
        killUnits(toKill);
    }


    public int getBuildingCost(BUILDING_TYPE type, int playerId) {
        int cost = BUILDING_COST(type);
        if (type == BUILDING_TYPE.TOWER) {
            return cost;
        } else { // == MINE
            for (Building building : this.buildings) {
                if (building.getType() == BUILDING_TYPE.MINE && building.getOwner() == playerId) {
                    cost += MINE_INCREMENT;
                }
            }
            return cost;
        }
    }

    private void computeIncome(int playerId) {
        int updatedIncome = 0;

        // increments golds of player
        for (int x = 0; x < MAP_WIDTH; ++x) {
            for (int y = 0; y < MAP_HEIGHT; ++y) {
                if (map[x][y].getOwner() == playerId && map[x][y].isActive())
                    updatedIncome += CELL_INCOME;
            }
        }

        // increments golds for active mines
        for (Building building : this.buildings) {
            if (building.getOwner() == playerId && building.getType() == BUILDING_TYPE.MINE && building.getCell().isActive())
                updatedIncome += MINE_INCOME;
        }

        // decrement for units
        for (Unit unit : this.units.values()) {
            if (unit.getOwner() == playerId && unit.isAlive())
                updatedIncome -= UNIT_UPKEEP[unit.getLevel()];
        }

        //update player income
        this.playerIncome.get(playerId).set(updatedIncome);
    }

    private void computeGold(int playerId) {
        // add player income to their gold (can be negative).
        this.playerGolds.get(playerId).addAndGet(this.playerIncome.get(playerId).intValue());
    }

    private void negativeGoldWipeout(int playerId) {
        // Negative amount of gold: kill all units and reset to 0
        this.playerGolds.get(playerId).set(0);

        List<Unit> toKill = new ArrayList<>();
        this.units.forEach((key, unit)-> {if (unit.isAlive() && unit.getOwner() == playerId) toKill.add(unit); });
        killUnits(toKill);
    }

    // action methods
    public void addUnit(Unit unit) {
        // TRAIN method
        // kill previous unit
        clearCell(unit.getCell());

        this.units.put(unit.getId(), unit);
        unit.getCell().setOwner(unit.getOwner());
        unit.getCell().setUnit(unit);
        this.playerGolds.get(unit.getOwner()).addAndGet(-UNIT_COST[unit.getLevel()]);
        for (int i = 0; i < PLAYER_COUNT; ++i)
            this.computeIncome(i);
    }

    public void moveUnit(Unit unit, Cell newPosition) {
        // MOVE method
        // free current cell
        clearCell(newPosition);

        unit.getCell().setUnit(null);
        unit.moved();
        unit.setX(newPosition.getX());
        unit.setY(newPosition.getY());
        unit.setCell(newPosition);

        newPosition.setOwner(unit.getOwner());
        // occupy new cell
        newPosition.setUnit(unit);

        for (int i = 0; i < PLAYER_COUNT; ++i)
            this.computeIncome(i);
    }

    public void addBuilding(Building building) {
        this.buildings.add(building);
        building.getCell().setBuilding(building);
        int cost = getBuildingCost(building.getType(), building.getOwner());

        // since the mine was already created, getBuildingCost returns MINE_INCREMENT too many
        if (building.getType() == BUILDING_TYPE.MINE) {
            cost -= MINE_INCREMENT;
        }
        this.playerGolds.get(building.getOwner()).addAndGet(-cost);
    }


    // referee methods
    private void sendMap(Player player) {
        for (int y = 0; y < MAP_HEIGHT; ++y) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < MAP_WIDTH; ++x) {
                int owner = this.map[x][y].getOwner();
                char data;
                switch (owner) {
                    case VOID:
                        data = '#';
                        break;
                    case NEUTRAL:
                        data = '.';
                        break;
                    default:
                        // o for own player, x for opponent
                        if (owner == player.getIndex()) {
                            data = 'o';
                        } else {
                            data = 'x';
                        }
                        // capital letter iif active cell
                        if (this.map[x][y].isActive()) {
                            data = Character.toUpperCase(data);
                        }
                }
                line.append(data);
            }
            player.sendInputLine(line.toString());
        }
    }

    private void sendMineSpots(Player player) {
        for (int y = 0; y < MAP_HEIGHT; ++y) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < MAP_WIDTH; ++x) {
                int owner = this.map[x][y].getOwner();
                char data;
                switch (owner) {
                    case VOID:
                        data = '#';
                        break;
                    case NEUTRAL:
                        data = '.';
                        break;
                    default:
                        // o for own player, x for opponent
                        if (owner == player.getIndex()) {
                            data = 'o';
                        } else {
                            data = 'x';
                        }
                        // capital letter iif active cell
                        if (this.map[x][y].isActive()) {
                            data = Character.toUpperCase(data);
                        }
                }
                line.append(data);
            }
            player.sendInputLine(line.toString());
        }
    }

    private void sendUnits(Player player) {
        // send unit count
        player.sendInputLine(String.valueOf(this.units.size()));

        List<Unit> unitList = new ArrayList<>(this.units.values());

        Collections.sort(unitList, new Comparator<Unit>() {
            @Override
            public int compare(Unit unit1, Unit unit2) {
                int currentOwner1 = (unit1.getOwner() - player.getIndex() + PLAYER_COUNT) % PLAYER_COUNT;
                int currentOwner2 = (unit2.getOwner() - player.getIndex() + PLAYER_COUNT) % PLAYER_COUNT;
                // not the same owner, building owned by 0 is sent first
                if (currentOwner1 != currentOwner2) {
                    return currentOwner1 - currentOwner2; // if o1 = 0, o1-o2 = -1, ie b1 < b2, else o1-o2 = 1, ie b2 < b1
                }

                return unit1.getId() - unit2.getId();
            }
        });

        // send units
        unitList.forEach(unit -> {
            StringBuilder line = new StringBuilder();
            line
                .append( (unit.getOwner() - player.getIndex() + PLAYER_COUNT) % PLAYER_COUNT) // always 0 for the player
                .append(" ")
                .append(unit.getId())
                .append(" ")
                .append(unit.getLevel())
                .append(" ")
                .append(unit.getX())
                .append(" ")
                .append(unit.getY());
            player.sendInputLine(line.toString());
        });
    }

    private void sendBuildings(Player player) {
        // send building count
        player.sendInputLine(String.valueOf(this.buildings.size()));
        
        Collections.sort(this.buildings, new Comparator<Building>() {
            @Override
            public int compare(Building building1, Building building2) {
                int currentOwner1 = (building1.getOwner() - player.getIndex() + PLAYER_COUNT) % PLAYER_COUNT;
                int currentOwner2 = (building2.getOwner() - player.getIndex() + PLAYER_COUNT) % PLAYER_COUNT;
                // not the same owner, building owned by 0 is sent first
                if (currentOwner1 != currentOwner2) {
                    return currentOwner1 - currentOwner2; // if o1 = 0, o1-o2 = -1, ie b1 < b2, else o1-o2 = 1, ie b2 < b1
                }

                // different type
                if (building1.getIntType() != building2.getIntType()) {
                    return building1.getIntType() - building2.getIntType();
                }

                // last case: order by x then y
                return (building1.getX() - building2.getX()) * MAP_HEIGHT + (building1.getY() - building2.getY());
            }
        });

        this.buildings.forEach(building -> {
            StringBuilder line = new StringBuilder();
            line
                .append( (building.getOwner() - player.getIndex() + PLAYER_COUNT) % PLAYER_COUNT ) // always 0 for the player
                .append(" ")
                .append(building.getIntType())
                .append(" ")
                .append(building.getX())
                .append(" ")
                .append(building.getY());
            player.sendInputLine(line.toString());
        });
    }

    public void sendState(Player player) {
        // send gold and income for current player
        player.sendInputLine(String.valueOf(this.playerGolds.get(player.getIndex())));
        player.sendInputLine(String.valueOf(this.playerIncome.get(player.getIndex())));

        // send gold and income for opponent player
        int opponentPlayerIndex = (player.getIndex() + 1) %PLAYER_COUNT;
        player.sendInputLine(String.valueOf(this.playerGolds.get(opponentPlayerIndex)));
        player.sendInputLine(String.valueOf(this.playerIncome.get(opponentPlayerIndex)));

        sendMap(player);
        sendBuildings(player);
        sendUnits(player);
    }

    public void debugViews() {
        // System.err.println(buildings.size() + " buildings, " + units.size() + " units");
        // units.forEach((id, unit) -> {System.err.println(unit.getId() + ": " +unit.getX() + " " + unit.getY());});
    }

    public List<AtomicInteger> getScores() {
        List<AtomicInteger> scores = new ArrayList<>(this.playerGolds);
        this.units.forEach((id, unit) -> {
            scores.get(unit.getOwner()).addAndGet(UNIT_COST[unit.getLevel()]);
        });
        return scores;
    }
}