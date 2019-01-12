package com.codingame.game;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;

import com.codingame.antiyoy.GameState;
import com.codingame.antiyoy.view.ViewController;

import com.google.inject.Inject;

import static com.codingame.antiyoy.Constants.*;
import com.codingame.antiyoy.Cell;
import com.codingame.antiyoy.Unit;
import com.codingame.antiyoy.Action;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;

    private GameState gameState;

    private ViewController viewController;

    private List<Action> actionList = new ArrayList<>();

    private int currentPlayer = 0;

    private int realTurn = 0;

    @Override
    public void init() {
        // send map size
        sendInitialInput();

        // Initialize your game here.
        this.gameState = new GameState();

        this.gameManager.setMaxTurns(2000); // the turns are determined by realTurns

        for (int x = 0; x < MAP_WIDTH; ++x) {
            for (int y = 0; y < MAP_HEIGHT; ++y) {
                double random = Math.random() * 100;
                int owner = random < 20 ? -2 : -1;
                this.gameState.getCell(x, y).setOwner(owner);
                this.gameState.getSymmetricCell(x, y).setOwner(owner);
            }
        }


        this.gameState.computeNeighbours();

        this.gameState.getCell(0, 0).setOwner(0);
        this.gameState.getCell(MAP_WIDTH-1, MAP_HEIGHT-1).setOwner(1);

        Unit unit0 = new Unit(0, 0, 0, 1);
        Unit unit1 = new Unit(MAP_WIDTH-1, MAP_HEIGHT-1, 1, 1);
        this.gameState.addUnit(unit0);
        this.gameState.addUnit(unit1);

        // Initialize viewer
        initializeView();
    }

    private void initializeView() {
        // init
        this.viewController = new ViewController(graphicEntityModule, gameManager.getPlayers(), this.gameState);

        // add all cells
        for (int x = 0; x < MAP_WIDTH; ++x)
            for (int y = 0; y < MAP_HEIGHT; ++y)
                this.viewController.createCellView(this.gameState.getCell(x, y));

        for (int idx = 0; idx < this.gameState.getUnitsSize(); ++idx)
                this.viewController.createUnitView(this.gameState.getUnit(idx));

        // display grid
        updateView();
    }

    private void updateView() {
        viewController.update();
    }

    @Override
    public void gameTurn(int turn) {
        if (hasAction()) {
            gameManager.setFrameDuration(400);
            forceAnimationFrame();
            Action action = this.actionList.get(0);
            this.actionList.remove(0);

            Player player = gameManager.getPlayer(action.getPlayer());

            if (!gameState.getCell(action.getX(), action.getY()).isFree()) {
                gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (cell occupied) " + action);
                System.err.println("Invalid occupied: " + action + " " + action.getX() + " " + action.getY() + " " + action.getPlayer());
                return;
            }
            if (!gameState.getCell(action.getX(), action.getY()).isPlayable(player.getIndex())) {
                gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (out of range) " + action);
                System.err.println("Invalid oor: " + action + " " + action.getX() + " " + action.getY() + " " + action.getPlayer());
                return;
            }

            if (action.getType() == ACTIONTYPE.TRAIN && gameState.getGold(player.getIndex()) < UNIT_COST[action.getLevel()]) {
                gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (not enough gold) " + action);
                return;
            }


            if (action.getType() == ACTIONTYPE.MOVE) {
                int unitId = action.getUnitId();
                Unit unit = gameState.getUnit(unitId);
                this.gameState.moveUnit(unit, action.getX(), action.getY());
                gameManager.addToGameSummary(player.getNicknameToken() + " moved " + unitId + " to (" + action.getX() + ", " + action.getY() + ")");

                updateView();
            }

            if (action.getType() == ACTIONTYPE.TRAIN) {
                Unit unit = new Unit(action.getX(), action.getY(), action.getPlayer(), 2);
                this.gameState.addUnit(unit);
                viewController.createUnitView(unit);
                gameManager.addToGameSummary(player.getNicknameToken() + " trained a unit in (" + action.getX() + ", " + action.getY() + ")");
                updateView();
            }
        }

        else {
            forceGameFrame();
            gameManager.setFrameDuration(1000);

            Player player = gameManager.getPlayer(this.currentPlayer);

            this.currentPlayer = (this.currentPlayer + 1) % PLAYER_COUNT;
            if (this.currentPlayer == 0) {
                this.realTurn++;
                if (this.realTurn > MAX_TURNS) {
                    gameManager.endGame();
                    return;
                }
            }

            /// send input
            sendInput(player);

            player.execute();

            // read input
            readInput(player);
            // end turn
            this.gameState.endTurn();
            // update viewer
            updateView();

        }
    }

    private boolean hasAction() {
        return !actionList.isEmpty();
    }

    private void forceAnimationFrame() {
        for (Player player : gameManager.getPlayers()) {
            player.setExpectedOutputLines(0);
            player.execute();
            try {
                player.getOutputs();
            } catch (Exception e) {
                // should not occur
            }
        }
    }

    private void forceGameFrame() {
        for (Player player : gameManager.getActivePlayers()) {
            player.setExpectedOutputLines(1);
        }
    }

    private void sendInitialInput() {
        StringBuilder firstLine = new StringBuilder();
        firstLine.append(MAP_WIDTH);
        firstLine.append(" ");
        firstLine.append(MAP_HEIGHT);
        for (Player player : gameManager.getActivePlayers())
            player.sendInputLine(firstLine.toString());
    }


    private void sendInput(Player player) {
        // send gold
        player.sendInputLine(String.valueOf(gameState.getGold(player.getIndex())));

        // send map
        for (int y = 0; y < MAP_HEIGHT; ++y) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < MAP_WIDTH; ++x) {
                int owner = gameState.getCell(x, y).getOwner();
                if (owner >= 0)
                    owner = (owner  - player.getIndex() + PLAYER_COUNT) % PLAYER_COUNT;
                line.append(owner);
                if (x != MAP_WIDTH - 1) {
                    line.append(" ");
                }
            }
            // System.err.println(line.toString());
            player.sendInputLine(line.toString());
        }

        // send unit count
        player.sendInputLine(String.valueOf(gameState.getUnitsSize()));

        // send units
        for (Unit unit : gameState.units) {
            StringBuilder line = new StringBuilder();
            line.append(unit.getId());
            line.append(" ");
            line.append( (unit.getOwner() - player.getIndex() + PLAYER_COUNT) % PLAYER_COUNT); // always 0 for the player
            line.append(" ");
            line.append(unit.getLevel());
            line.append(" ");
            line.append(unit.getX());
            line.append(" ");
            line.append(unit.getY());
            player.sendInputLine(line.toString());
        }
    }


    private void readInput(Player player) {
        try {
            List<String> outputs = player.getOutputs();
            if (outputs.isEmpty()) {
                System.err.println("Empty outputs: " + this.realTurn);
                return;
            }
            String[] actions = outputs.get(0).split(";");

            for (String actionStr : actions) {
                actionStr = actionStr.trim();
                Matcher moveMatcher = MOVE_PATTERN.matcher(actionStr);
                Matcher buildMatcher = BUILD_PATTERN.matcher(actionStr);
                Matcher trainMatcher = TRAIN_PATTERN.matcher(actionStr);
                if (moveMatcher.find()) {
                    int id = Integer.parseInt(moveMatcher.group(1));
                    int x = Integer.parseInt(moveMatcher.group(2));
                    int y = Integer.parseInt(moveMatcher.group(3));

                    if (!gameState.isLegal(x, y)) {
                        gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (out of bound) " + actionStr);
                        System.err.println("Invalid oob: " + x + " " + y + " " + player.getIndex());
                        continue;
                    }
                    if (gameState.getUnit(id) == null) {
                        gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (invalid id) " + actionStr);
                        System.err.println("Invalid id: " + x + " " + y + " " + player.getIndex());
                        continue;
                    }

                    Action action = new Action(actionStr, ACTIONTYPE.MOVE, player.getIndex(), id, x, y);
                    this.actionList.add(action);

                } else if (buildMatcher.find()) {
                    String type = buildMatcher.group(1);
                    int x = Integer.parseInt(buildMatcher.group(2));
                    int y = Integer.parseInt(buildMatcher.group(3));
                    // TODO: implement buildings

                } else if (trainMatcher.find()) {
                    int level = Integer.parseInt(trainMatcher.group(1));
                    int x = Integer.parseInt(trainMatcher.group(2));
                    int y = Integer.parseInt(trainMatcher.group(3));
                    if (!gameState.isLegal(x, y)) {
                        gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (out of bound) " + actionStr);
                        continue;
                    }
                    if (level < 0 || level > MAX_LEVEL) {
                        gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (invalid level) " + actionStr);
                        continue;
                    }

                    Action action = new Action(actionStr, ACTIONTYPE.TRAIN, player.getIndex(), level, x, y);
                    this.actionList.add(action);
                } else {
                    gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action " + actionStr);
                }
            }
        } catch (TimeoutException e) {
            player.deactivate(String.format("$%d timeout!", player.getIndex()));
        }
    }
}
