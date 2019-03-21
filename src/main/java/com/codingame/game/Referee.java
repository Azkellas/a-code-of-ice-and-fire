package com.codingame.game;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codingame.antiyoy.*;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;

import com.codingame.antiyoy.view.ViewController;

import com.google.inject.Inject;

import static com.codingame.antiyoy.Constants.*;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    @Inject private EndScreenModule endScreenModule;
    @Inject private TooltipModule tooltipModule;

    private GameState gameState;

    private LEAGUE league;

    private ViewController viewController;

    private List<Action> actionList = new ArrayList<>();

    private int currentPlayer = -1;

    private int realTurn = 0;

    @Override
    public void onEnd() {
        //int[] scores = new int[2];
        //scores[0] = gameManager.getPlayer(0).getScore();
        //scores[1] = gameManager.getPlayer(1).getScore();
        String[] text = {"", ""};
        if (gameManager.getPlayer(0).getScore() == -1 || gameManager.getPlayer(1).getScore() == -1) {
            // timeout or capture: no need to display scores
            text[0] = " ";
            text[1] = " ";
        }
        endScreenModule.setScores(gameManager.getPlayers().stream().mapToInt(p -> p.getScore()).toArray(), text);
    }

    @Override
    public void init() {
        // send map size
        sendInitialInput();

        // Initialize your game here.
        this.gameState = new GameState(this.gameManager.getSeed());
        // this.endScreenModule = new EndScreenModule();
        this.gameManager.setMaxTurns(1000000); // Turns are determined by realTurns, this is actually maxFrames

        this.gameManager.setFrameDuration(400);

        // Get league
        switch (this.gameManager.getLeagueLevel()) {
            case 1:
                this.league = LEAGUE.WOOD3;
                // Only T1 units, no building
                break;
            case 2:
                this.league = LEAGUE.WOOD2;
                // Now T2/T3 units, kill mechanism
                break;
            case 3:
                this.league = LEAGUE.WOOD1;
                // Now Mines
                break;
            default:
                this.league = LEAGUE.BRONZE;
                // Now Towers
        }

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
        this.viewController = new ViewController(graphicEntityModule, tooltipModule, gameManager.getPlayers(), this.gameState);

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
            makeAction();
        }
        else {
            // get current player
            if (!computeCurrentPlayer()) {
                return;
            }
            Player player = gameManager.getPlayer(this.currentPlayer);

            // compute new golds / zones / killed units
            gameState.initTurn(this.currentPlayer);

            /// Send input
            sendInput(player);
            player.execute();

            // Read and parse answer
            readInput(player);

            // Make the first action to save frames
            if (hasAction()) {
                makeAction();
            }
        }
        updateView();
        checkForHqCapture();
    }

    // return true if there is a next player, false if this is the end of the game
    private boolean computeCurrentPlayer() {
        this.currentPlayer = (this.currentPlayer + 1) % PLAYER_COUNT;
        if (this.currentPlayer == 0) {
            this.realTurn++;
            System.out.println("Turn: " + this.realTurn);
            if (this.realTurn > MAX_TURNS) {
                discriminateEndGame();
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

        // Free cell or killable unit / destroyable building
        if (!action.getCell().isCapturable(action.getPlayer(), unit.getLevel())) {
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

        // Free cell or killable unit / destroyable building
        if (!action.getCell().isCapturable(action.getPlayer(), action.getLevel())) {
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

        if (league == LEAGUE.WOOD3 || league == LEAGUE.WOOD2) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (no building in this league) " + action);
            return;
        }

        if (league == LEAGUE.WOOD1 && action.getBuildType() == BUILDING_TYPE.TOWER) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (no Tower in this league) " + action);
            return;
        }

        if (!action.getCell().isFree()) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (cell occupied) " + action);
            return;
        }

        if (action.getCell().getOwner() != action.getPlayer()) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (cell not owned) " + action);
            return;
        }

        if (gameState.getGold(player.getIndex()) < BUILDING_COST(action.getBuildType())) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (not enough gold) " + action);
            return;
        }

        Building building = new Building(action.getCell(), action.getPlayer(), action.getBuildType());
        this.gameState.addBuilding(building);
        viewController.createBuildingView(building);
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
    }

    private void sendInitialInput() {
        StringBuilder firstLine = new StringBuilder();
        firstLine.append(MAP_WIDTH);
        StringBuilder secondLine = new StringBuilder();
        secondLine.append(MAP_HEIGHT);
        for (Player player : gameManager.getActivePlayers()) {
            player.sendInputLine(firstLine.toString());
            player.sendInputLine(secondLine.toString());
        }
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

                if (actionStr.equals("WAIT")) {
                    continue;
                }

                if (!matchMoveTrain(player, actionStr) && !matchBuild(player, actionStr)) {
                    // unrecognized pattern: timeout
                    gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (unknown pattern) " + actionStr);
                    // clear actions
                    actionList.clear();
                    player.deactivate(String.format("$%d timeout!", player.getIndex()));
                    checkForEndGame();
                }
            }
        } catch (TimeoutException e) {
            player.deactivate(String.format("$%d timeout!", player.getIndex()));
            checkForEndGame();
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
            if (league == LEAGUE.WOOD3 && idOrLevel != 1) {
                gameManager.addToGameSummary(player.getNicknameToken() + ": expected a level 1 in wood 3 " + actionStr);
                return true;
            }
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

        String typeStr = buildMatcher.group(1);
        BUILDING_TYPE buildType = Building.convertType(typeStr);
        int x = Integer.parseInt(buildMatcher.group(2));
        int y = Integer.parseInt(buildMatcher.group(3));

        if (!gameState.isInside(x, y)) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (out of bound) " + actionStr);
            return true;
        }

        Action action = new Action(actionStr, ACTIONTYPE.BUILD, player.getIndex(), this.gameState.getCell(x, y), buildType);
        this.actionList.add(action);
        return true;
    }


    private void checkForHqCapture() {
        for (Building HQ : this.gameState.getHQs()) {
            if (HQ.getCell().getOwner() != HQ.getOwner()) {
                int playerIdx = HQ.getOwner();
                gameManager.getPlayer(playerIdx).deactivate();
                checkForEndGame();
            }
        }
    }
    private void checkForEndGame() {
        if (!gameManager.getPlayer(0).isActive()) {
            // score = rank
            gameManager.getPlayer(0).setScore(-1);
            gameManager.getPlayer(1).setScore(1);
            gameManager.endGame();
        }
        if (!gameManager.getPlayer(1).isActive()) {
            // score = rank
            gameManager.getPlayer(1).setScore(-1);
            gameManager.getPlayer(0).setScore(1);
            gameManager.endGame();
        }
    }

    private void discriminateEndGame() {
        List<AtomicInteger> scores = this.gameState.getScores();
        gameManager.getPlayer(0).setScore(scores.get(0).intValue());
        gameManager.getPlayer(1).setScore(scores.get(1).intValue());
        gameManager.endGame();
    }
}
