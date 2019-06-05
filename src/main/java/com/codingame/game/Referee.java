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

    private AtomicInteger currentPlayer;

    private AtomicInteger realTurn;

    @Override
    public void onEnd() {
        //int[] scores = new int[2];
        //scores[0] = gameManager.getPlayer(0).getScore();
        //scores[1] = gameManager.getPlayer(1).getScore();
        if (gameManager.getPlayer(0).getScore() == -1 || gameManager.getPlayer(1).getScore() == -1) {
            // timeout or capture: no need to display scores
            String[] text = {};
            endScreenModule.setScores(gameManager.getPlayers().stream().mapToInt(p -> p.getScore()).toArray(), text);
        } else {
            endScreenModule.setScores(gameManager.getPlayers().stream().mapToInt(p -> p.getScore()).toArray());
        }
    }

    @Override
    public void init() {
        // this.endScreenModule = new EndScreenModule();
        this.gameManager.setMaxTurns(1000000); // Turns are determined by realTurns, this is actually maxFrames

        this.gameManager.setFrameDuration(400);

        this.currentPlayer = new AtomicInteger(-1);
        this.realTurn = new AtomicInteger(0);

        // Get league
        switch (this.gameManager.getLeagueLevel()) {
            case 1:
                // Only T1 units, no building
                MAX_MOVE_LENGTH = 1;
                MAX_LEVEL = 1;
                this.league = LEAGUE.WOOD3;
                break;
            case 2:
                // Now T2/T3 units, kill mechanism
                MAX_MOVE_LENGTH = 1;
                this.league = LEAGUE.WOOD2;
                break;
            case 3:
                // Now Mines are unlocked
                MAX_MOVE_LENGTH = 1;
                this.league = LEAGUE.WOOD1;
                break;
            default:
                // All rules
                this.league = LEAGUE.BRONZE;
        }

        this.gameState = new GameState(this.gameManager.getSeed(), this.league);

        // Random generation
        this.gameState.generateMap(this.league);

        try {
            this.gameState.createHQs(this.gameManager.getPlayerCount());
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // send map size and mine spots
        sendInitialInput();

        // Initialize viewer
        initializeView();
    }

    private void initializeView() {
        // Init
        this.viewController = new ViewController(graphicEntityModule, tooltipModule, gameManager.getPlayers(), this.gameState, this.realTurn, this.currentPlayer);

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
            boolean madeAnAction = false;
            while (hasAction() && !madeAnAction) {
                madeAnAction = makeAction();
            }
        }
        else {
            // get current player
            if (!computeCurrentPlayer()) {
                return;
            }
            Player player = gameManager.getPlayer(this.currentPlayer.intValue());
            player.resetMessage();
            // compute new golds / zones / killed units
            gameState.initTurn(this.currentPlayer.intValue());

            /// Send input
            sendInput(player);
            player.execute();

            // Read and parse answer
            readInput(player);

            /*
            // Make the first action to save frames
            boolean madeAnAction = false;
            while (hasAction() && !madeAnAction) {
                madeAnAction = makeAction();
            }*/
        }
        updateView();
        checkForHqCapture();
    }

    // return true if there is a next player, false if this is the end of the game
    private boolean computeCurrentPlayer() {
        this.currentPlayer.set( (this.currentPlayer.intValue() + 1) % PLAYER_COUNT);
        if (this.currentPlayer.intValue() == 0) {
            this.realTurn.addAndGet(1);
            // System.out.println("Turn: " + this.realTurn);
            if (this.realTurn.intValue() > MAX_TURNS) {
                discriminateEndGame();
                return false;
            }
        }
        return true;
    }

    private boolean hasAction() {
        return !actionList.isEmpty();
    }


    private boolean makeMoveAction(Action action) {
        Player player = gameManager.getPlayer(action.getPlayer());

        if (!gameState.getUnit(action.getUnitId()).canPlay()) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (unit already moved) " + action);
            return false;
        }

        int unitId = action.getUnitId();
        Unit unit = gameState.getUnit(unitId);

        // Free cell or killable unit / destroyable building
        if (!action.getCell().isCapturable(action.getPlayer(), unit.getLevel()) && Math.abs(unit.getX() - action.getCell().getX()) + Math.abs(unit.getY() - action.getCell().getY()) == 1) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (cell occupied) " + action);
            return false;
        }

        Cell nextCell = this.gameState.getNextCell(unit, action.getCell());

        if (nextCell.getX() == unit.getCell().getX() && nextCell.getY() == unit.getCell().getY()) {
            // no MOVE to do
            gameManager.addToGameSummary(player.getNicknameToken() + ": Unit " + unitId + " stayed still (no nearest cell in range) " + action);
            return false;
        }
        this.gameState.moveUnit(unit, nextCell);
        this.gameState.computeAllActiveCells();
        gameManager.addToGameSummary(player.getNicknameToken() + " moved " + unitId + " to (" + nextCell.getX() + ", " + nextCell.getY() + ")");
        for (int i = 0; i < PLAYER_COUNT; ++i)
            gameState.computeIncome(i);
        return true;
    }

    private boolean makeTrainAction(Action action) {
        Player player = gameManager.getPlayer(action.getPlayer());

        if (action.getLevel() > MAX_LEVEL) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (the level must be less than " + action.getLevel() + ") " + action);
            return false;
        }

        if (gameState.getGold(player.getIndex()) < UNIT_COST[action.getLevel()]) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (not enough gold) " + action);
            return false;
        }

        // Free cell or killable unit / destroyable building
        if (!action.getCell().isCapturable(action.getPlayer(), action.getLevel())) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (cell occupied) " + action);
            return false;
        }

        Unit unit = new Unit(action.getCell(), action.getPlayer(), action.getLevel());
        this.gameState.addUnit(unit);
        this.gameState.computeAllActiveCells();
        viewController.createUnitView(unit);
        gameManager.addToGameSummary(player.getNicknameToken() + " trained a unit in (" + action.getCell().getX() + ", " + action.getCell().getY() + ")");
        for (int i = 0; i < PLAYER_COUNT; ++i)
            gameState.computeIncome(i);
        return true;
    }

    private boolean makeBuildAction(Action action) {
        Player player = gameManager.getPlayer(action.getPlayer());

        if (league == LEAGUE.WOOD3 || league == LEAGUE.WOOD2) {
            String message = "Invalid action (no building in this league)";

            if (league == LEAGUE.WOOD2 && action.getBuildType() == BUILDING_TYPE.TOWER)
                message = "Invalid action (no TOWER in this league)";

            gameManager.addToGameSummary(player.getNicknameToken() + ": " + message + " " + action);
            return false;
        }

        if (!action.getCell().isFree()) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (cell occupied) " + action);
            return false;
        }

        if (action.getCell().getOwner() != action.getPlayer()) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (cell not owned) " + action);
            return false;
        }

        if (gameState.getGold(player.getIndex()) < this.gameState.getBuildingCost(action.getBuildType(), action.getPlayer())) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (not enough gold) " + action);
            return false;
        }

        if (action.getBuildType() == BUILDING_TYPE.MINE && !action.getCell().isMineSpot()) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (not a mine spot) " + action);
            return false;
        }

        if (action.getBuildType() == BUILDING_TYPE.TOWER && action.getCell().isMineSpot()) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (cannot build tower on mine spot) " + action);
            return false;
        }

        Building building = new Building(action.getCell(), action.getPlayer(), action.getBuildType());
        this.gameState.addBuilding(building);
        viewController.createBuildingView(building);
        if (action.getBuildType() == BUILDING_TYPE.MINE)
            gameManager.addToGameSummary(player.getNicknameToken() + " built a MINE in (" + action.getCell().getX() + ", " + action.getCell().getY() + ")");
        else if (action.getBuildType() == BUILDING_TYPE.TOWER)
            gameManager.addToGameSummary(player.getNicknameToken() + " built a TOWER in (" + action.getCell().getX() + ", " + action.getCell().getY() + ")");
        for (int i = 0; i < PLAYER_COUNT; ++i)
            gameState.computeIncome(i);
        return true;
    }

    private boolean makeAction() {
        Action action = this.actionList.get(0);
        this.actionList.remove(0);

        Player player = gameManager.getPlayer(action.getPlayer());

        // TRAIN & BUILD can only be done on a playable cell
        // MOVE on the contrary can target any cell
        if (action.getType() != ACTIONTYPE.MOVE && !action.getCell().isPlayable(player.getIndex())) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (cell not playable) " + action);
            return false;
        }


        if (action.getType() == ACTIONTYPE.MOVE) {
            return makeMoveAction(action);
        } else if (action.getType() == ACTIONTYPE.TRAIN) {
            return makeTrainAction(action);
        } else { // ACTIONTYPE.BUILD
            return makeBuildAction(action);
        }
    }

    private void sendInitialInput() {
        StringBuilder nbMineSpots = new StringBuilder();
        nbMineSpots.append(gameState.getNbMineSpots());


        for (Player player : gameManager.getActivePlayers()) {
            player.sendInputLine(nbMineSpots.toString());

            // send mine spots
            for (int y = 0; y < MAP_HEIGHT; ++y) {
                for (int x = 0; x < MAP_WIDTH; ++x) {
                    if (this.gameState.getCell(x, y).isMineSpot()) {
                        StringBuilder mineSpot = new StringBuilder();
                        mineSpot.append(x).append(" ").append(y);
                        player.sendInputLine(mineSpot.toString());
                    }
                }
            }
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

                if (matchMsg(player, actionStr)) {
                    // set message
                    continue;
                }

                if (!matchMoveTrain(player, actionStr) && !matchBuild(player, actionStr)) {
                    // unrecognized pattern: timeout
                    gameManager.addToGameSummary(player.getNicknameToken() + ": Unrecognised command (\"" + actionStr+"\")");
                    // clear actions
                    actionList.clear();
                    player.deactivate(String.format("$%d: unrecognised command (\"%s\")", player.getIndex(), actionStr));
                    checkForEndGame();
                    break;
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

    private boolean matchMsg(Player player, String actionStr) {
        Matcher msgMatcher = MSG_PATTERN.matcher(actionStr);
        if (!msgMatcher.find()) {
            return false;
        }

        try {
            String message = actionStr.substring(4);
            player.setMessage(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean matchMoveTrain(Player player, String actionStr) {
        Matcher moveTrainMatcher = MOVETRAIN_PATTERN.matcher(actionStr);
        if (!moveTrainMatcher.find())
            return false;

        ACTIONTYPE type = (moveTrainMatcher.group(1).equals("TRAIN")) ? ACTIONTYPE.TRAIN : ACTIONTYPE.MOVE;
        int idOrLevel;
        int x;
        int y;

        try {
            idOrLevel = Integer.parseInt(moveTrainMatcher.group(2));
            x = Integer.parseInt(moveTrainMatcher.group(3));
            y = Integer.parseInt(moveTrainMatcher.group(4));
        } catch (NumberFormatException e) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (out of bounds) " + actionStr);
            return true;
        }
    
        if (!gameState.isInside(x, y)) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (out of bounds) " + actionStr);
            return true;
        }

        if (type == ACTIONTYPE.TRAIN) {
            if (league == LEAGUE.WOOD3 && idOrLevel != 1) {
                gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (only level 1 units in this league) " + actionStr);
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
        int x;
        int y;

        try {
            x = Integer.parseInt(buildMatcher.group(2));
            y = Integer.parseInt(buildMatcher.group(3));
        } catch (NumberFormatException e) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (out of bounds) " + actionStr);
            return true;
        }

        if (!gameState.isInside(x, y)) {
            gameManager.addToGameSummary(player.getNicknameToken() + ": Invalid action (out of bounds) " + actionStr);
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
            // score = military value
            List<AtomicInteger> scores = this.gameState.getScores();
            gameManager.getPlayer(0).setScore(-1);
            gameManager.getPlayer(1).setScore(scores.get(1).intValue());
            gameManager.endGame();
        }
        if (!gameManager.getPlayer(1).isActive()) {
            // score = military value
            List<AtomicInteger> scores = this.gameState.getScores();
            gameManager.getPlayer(1).setScore(-1);
            gameManager.getPlayer(0).setScore(scores.get(0).intValue());
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
