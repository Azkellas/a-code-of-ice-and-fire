import com.codingame.antiyoy.Action;
import com.codingame.antiyoy.Cell;
import com.codingame.antiyoy.Constants;
import com.codingame.antiyoy.Unit;

import java.util.Scanner;

import static com.codingame.antiyoy.Constants.*;

public class Agent1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            System.out.println("TRAIN 1 5 4; MOVE 1  3;");




//            // Check validity of the player output and compute the new game state
//            int unitsSize = this.gameState.getUnitsSize();
//            for (int idx = 0; idx < unitsSize; ++idx) {
//                Unit unit = this.gameState.getUnit(idx);
//                if (unit.getOwner() != player.getIndex())
//                    continue;
//
//                Cell cell = this.gameState.getCell(unit.getX(), unit.getY());
//                for (int dir = 0; dir < 4; ++dir) {
//                    if (cell.neighbours[dir] != null && cell.neighbours[dir].getOwner() != unit.getOwner()) {
//                        this.actionList.add(new Action(Constants.ACTIONTYPE.MOVE, player.getIndex(), unit.getId(), cell.neighbours[dir].getX(), cell.neighbours[dir].getY()));
//                        break;
//                    }
//                }
//            }
//            int gold = this.gameState.getGold(player.getIndex());
//            boolean newBuild = true;
//            while (gold > UNIT_COST[2] && newBuild) {
//                newBuild = false;
//                outer:
//                for (int x = 0; x < MAP_WIDTH; ++x) {
//                    for (int y = 0; y < MAP_HEIGHT; ++y) {
//                        Cell cell = this.gameState.getCell(x, y);
//                        if (cell.getOwner() == VOID || cell.getOwner() == player.getIndex())
//                            continue;
//
//                        for (Cell neighbour : cell.neighbours) {
//                            if (neighbour != null && neighbour.getOwner() == player.getIndex()) {
//                                // build on cell
//                                this.actionList.add(new Action(ACTIONTYPE.TRAIN, player.getIndex(), x, y));
//                                gold -= UNIT_COST[2];
//                                newBuild = true;
//                                break outer;
//                            }
//                        }
//                    }
//                }
//            }

        }
    }
}
