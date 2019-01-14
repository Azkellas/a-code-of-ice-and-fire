package com.codingame.game;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codingame.antiyoy.*;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;

import com.codingame.antiyoy.view.ViewController;

import com.google.inject.Inject;

import static com.codingame.antiyoy.Constants.*;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;

    private GameState gameState;

    private ViewController viewController;

    private List<Action> actionList = new ArrayList<>();

    private int currentPlayer = -1;

    private int realTurn = 0;

    @Override
    public void init() {
        // send map size
        sendInitialInput();

        // Initialize your game here.
        this.gameState = new GameState();

        this.gameManager.setMaxTurns(2000); // Turns are determined by realTurns, this is actually maxFrames

        // Random generation
        this.gameState.generateMap();

        try {
            this.gameState.createHQs(this.gameManager.getPlayerCount());
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Initialize viewer
        initializeView();
    }

    private void initializeView() {
        // Init
        this.viewController = new ViewController(graphicEntityModule, gameManager.getPlayers(), this.gameState);

        // Add all cells
        for (int x = 0; x < MAP_WIDTH; ++x)
            for (int y = 0; y < MAP_HEIGHT; ++y)
                this.viewController.createCellView(this.gameState.getCell(x, y));

        // Add HQs
        for (Building HQ : this.gameState.getHQs())
            this.viewController.createBuildingView(HQ);

        // Display grid
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
            makeAction();
        }
        else {
            forceGameFrame();
            gameManager.setFrameDuration(400);

            // get current player
            if (!computeCurrentPlayer())
                return;
            Player player = gameManager.getPlayer(this.currentPlayer);

            // compute new golds / zones / killed units
            gameState.initTurn(this.currentPlayer);
            updateView();

            /// Send input
            sendInput(player);
            player.execute();

            // Read and parse answer
            readInput(player);

            // Make the first action to save frames
            if (hasAction())
                makeAction();
        }
    }

    // return true if there is a next player, false if this is the end of the game
    private boolean computeCurrentPlayer() {
        this.currentPlayer = (this.currentPlayer + 1) % PLAYER_COUNT;
        if (this.currentPlayer == 0) {
            this.realTurn++;
            if (this.realTurn > MAX_TURNS) {
                gameManager.endGame();
                return false;
            }
        }
        return true;
    }

    private boolean hasAction() {
        return !actionList.isEmpty();
    }


    private void makeMoveAction(Action action) {
        Player player = gameManager.getPlayer(action.getPlayer());

        if (!gameState.getUnit(action.getUnitId()).canPlay()) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (unit already moved) " + action);
            return;
        }

        int unitId = action.getUnitId();
        Unit unit = gameState.getUnit(unitId);

        if (!action.getCell().isCapturable(unit.getLevel())) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (cell occupied) " + action);
            return;
        }

        this.gameState.moveUnit(unit, action.getCell());
        this.gameState.computeAllActiveCells();
        gameManager.addToGameSummary(player.getNicknameToken() + " moved " + unitId + " to (" + action.getCell().getX() + ", " + action.getCell().getY() + ")");
    }

    private void makeTrainAction(Action action) {
        Player player = gameManager.getPlayer(action.getPlayer());

        if (gameState.getGold(player.getIndex()) < UNIT_COST[action.getLevel()]) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (not enough gold) " + action);
            return;
        }

        if (!action.getCell().isCapturable(action.getLevel())) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (cell occupied) " + action);
            return;
        }

        Unit unit = new Unit(action.getCell(), action.getPlayer(), action.getLevel());
        this.gameState.addUnit(unit);
        this.gameState.computeAllActiveCells();
        viewController.createUnitView(unit);
        gameManager.addToGameSummary(player.getNicknameToken() + " trained a unit in (" + action.getCell().getX() + ", " + action.getCell().getY() + ")");
    }

    private void makeBuildAction(Action action) {
        Player player = gameManager.getPlayer(action.getPlayer());

        if (!action.getCell().isFree()) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (cell occupied) " + action);
            return;
        }
        // TODO: implement BUILD action

    }
    private void makeAction() {
        Action action = this.actionList.get(0);
        this.actionList.remove(0);

        Player player = gameManager.getPlayer(action.getPlayer());

        if (!action.getCell().isPlayable(player.getIndex())) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (out of range) " + action);
            return;
        }


        if (action.getType() == ACTIONTYPE.MOVE) {
            makeMoveAction(action);
        } else if (action.getType() == ACTIONTYPE.TRAIN) {
            makeTrainAction(action);
        } else { // ACTIONTYPE.BUILD
            makeBuildAction(action);
        }

        updateView();
        checkForEndGame();
    }


    private void forceAnimationFrame() {
        for (Player player : gameManager.getPlayers()) {
            player.setExpectedOutputLines(0);
            player.execute();
            try {
                player.getOutputs();
            } catch (Exception e) {
                // should not occur since no output is required
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
        this.gameState.sendState(player);
    }


    private void readInput(Player player) {
        try {
            List<String> outputs = player.getOutputs();
            if (outputs.isEmpty()) {
                return;
            }

            String[] actions = outputs.get(0).split(";");

            for (String actionStr : actions) {
                actionStr = actionStr.trim();

                if (!matchMoveTrain(player, actionStr) && !matchBuild(player, actionStr))
                    gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (unknown pattern) " + actionStr);
            }
        } catch (TimeoutException e) {
            player.deactivate(String.format("$%d timeout!", player.getIndex()));
        }
    }


    private void createTrainAction(Player player, int level, int x, int y, String actionStr) {
        if (level <= 0 || level > MAX_LEVEL) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (invalid level) " + actionStr);
            return;
        }

        Action action = new Action(actionStr, ACTIONTYPE.TRAIN, player.getIndex(), level, this.gameState.getCell(x, y));
        this.actionList.add(action);
    }


    private void createMoveAction(Player player, int id, int x, int y, String actionStr) {
        if (gameState.getUnit(id) == null) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (invalid id) " + actionStr);
            return;
        }

        if (player.getIndex() != gameState.getUnit(id).getOwner()) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (not your unit) " + actionStr);
            return;
        }

        Action action = new Action(actionStr, ACTIONTYPE.MOVE, player.getIndex(), id, this.gameState.getCell(x, y));
        this.actionList.add(action);
    }


    private boolean matchMoveTrain(Player player, String actionStr) {
        Matcher moveTrainMatcher = MOVETRAIN_PATTERN.matcher(actionStr);
        if (!moveTrainMatcher.find())
            return false;

        ACTIONTYPE type = (moveTrainMatcher.group(1).equals("TRAIN")) ? ACTIONTYPE.TRAIN : ACTIONTYPE.MOVE;
        int idOrLevel = Integer.parseInt(moveTrainMatcher.group(2));
        int x = Integer.parseInt(moveTrainMatcher.group(3));
        int y = Integer.parseInt(moveTrainMatcher.group(4));

        if (!gameState.isInside(x, y)) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (out of bound) " + actionStr);
            return true;
        }

        if (type == ACTIONTYPE.TRAIN) {
            createTrainAction(player, idOrLevel, x, y, actionStr);
        } else { // MOVE
            createMoveAction(player, idOrLevel, x, y, actionStr);
        }

        return true;
    }


    private boolean matchBuild(Player player, String actionStr) {
        Matcher buildMatcher = BUILD_PATTERN.matcher(actionStr);
        if (!buildMatcher.find())
            return false;

        String type = buildMatcher.group(1);
        int x = Integer.parseInt(buildMatcher.group(2));
        int y = Integer.parseInt(buildMatcher.group(3));

        if (!gameState.isInside(x, y)) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (out of bound) " + actionStr);
            return true;
        }

        // TODO: implement buildings

        return true;
    }


    private void checkForEndGame() {
        for (Building HQ : this.gameState.getHQs()) {
            if (HQ.getCell().getOwner() != HQ.getOwner()) {
                int playerIdx = HQ.getCell().getOwner();
                // score = rank
                gameManager.getPlayer(playerIdx).setScore(2);
                gameManager.getPlayer(1 - playerIdx).setScore(1);
                gameManager.endGame();
            }
        }
    }
}
