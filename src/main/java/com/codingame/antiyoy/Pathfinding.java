package com.codingame.antiyoy;



import org.apache.commons.lang3.tuple.Pair;
// import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.naming.LinkLoopException;
import java.util.LinkedList;

import static com.codingame.antiyoy.Constants.*;

public class Pathfinding {
    private int[][] distances;

    private int MAP_SIZE = MAP_HEIGHT * MAP_WIDTH;
    private int INFINITY = MAP_SIZE + 1;

    public Pathfinding() {}

    public void init(Cell[][] map) {
        // init distances
        // distances wil store all cell to cell distances
        distances = new int[MAP_SIZE][MAP_SIZE];

        for (int i = 0; i < MAP_SIZE; ++i) {
            // set all distances to infinity
            for (int j = 0; j < MAP_SIZE; ++j) {
                distances[i][j] = INFINITY;
            }
            // set distance to self to 0
            distances[i][i] = 0;
        }

        // set distance to neighbours to 1
        for (int x = 0; x < MAP_WIDTH; ++x) {
            for (int y = 0; y < MAP_HEIGHT; ++y) {
                Cell currentCell = map[x][y];
                for (Cell neighbour : currentCell.getNeighbours()) {
                    if (neighbour != null) {
                        distances[getId(currentCell)][getId(neighbour)] = 1;
                    }
                }
            }
        }

        // perform Floyd-Warshall algorithm to compute all distances
        for (int k = 0; k < MAP_SIZE; ++k) {
            for (int i = 0; i < MAP_SIZE; ++i) {
                for (int j = 0; j < MAP_SIZE; ++j) {
                    if (distances[i][j] >= distances[i][k] + distances[k][j]) {
                        distances[i][j] = distances[i][k] + distances[k][j];
                    }
                }
            }
        }
    }

    public Cell getNearestCell(Cell[][] map, Unit unit, Cell target) {
        // here, we perform a bfs taking into account the current map configuration to find the best possible position
        Cell start = unit.getCell();
        int playerId = unit.getOwner();

        int startId = getId(start);
        int targetId = getId(target);

        // order is required to ensure symmetry
        int[] order;
        if (playerId == 0) {
            order = new int[] {UP, RIGHT, DOWN, LEFT};
        } else {
            order = new int[] {DOWN, LEFT, UP, RIGHT};
        }

        boolean[] visited = new boolean[MAP_SIZE];
        for (boolean bool : visited) {
            bool = false;
        }

        // left = depth, right = cellId
        // LinkedList<Pair<Integer, Integer>> queue = new LinkedList<>();
        LinkedList<Vector2> queue = new LinkedList<>();
        // queue.add(new ImmutablePair<> (0, getId(start)));
        queue.add(new Vector2(0, getId(start)));
        visited[startId] = true;


        Cell bestCell = start;
        int bestDistance = distances[startId][targetId];

        while (!queue.isEmpty()) {
            // Pair<Integer, Integer> pair = queue.pop();
            // int depth = pair.getLeft();
            // int cellId = pair.getRight();
            Vector2 pair = queue.pop();
            int depth = pair.getX();
            int cellId = pair.getY();
            Cell cell = getCell(map, cellId);

            // new best: free and nearer
            if (distances[cellId][targetId] < bestDistance && cell.isCapturable(playerId, unit.getLevel())) {
                bestDistance = distances[cellId][targetId];
                bestCell = cell;
            }

            if (depth < MAX_MOVE_LENGTH && cell.getOwner() == unit.getOwner()) {
                // we can move further
                for (int direction : order) {
                    Cell neighbour = cell.getNeighbour(direction);
                    if (neighbour != null) {
                        int neighbourId = getId(neighbour);
                        visited[neighbourId] = true;
                        // queue.add(new ImmutablePair<>(depth + 1, neighbourId));
                        queue.add(new Vector2(depth + 1, neighbourId));
                    }
                }
            }
        }

        return bestCell;
    }

    private int getId(int x, int y) {
        return x + MAP_WIDTH * y;
    }
    private int getId(Cell cell) {
        return getId(cell.getX(), cell.getY());
    }

    private Cell getCell(Cell[][] map, int cellId) {
        int x = cellId % MAP_WIDTH;
        int y = cellId / MAP_WIDTH;
        return map[x][y];
    }
}